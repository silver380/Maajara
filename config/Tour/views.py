from telnetlib import STATUS
from urllib import response
from rest_framework.generics import ListAPIView, GenericAPIView
from rest_framework.response import Response
from .serializers import TourCreatSerializer, UserInfoSerializer, TourSerializers
from rest_framework import permissions
from .permissions import IsTourLeader
from .models import Tour
from django.core import serializers
from django.contrib.auth import get_user_model

from django.forms.models import model_to_dict


# Create your views here.
class TourListAPIView(ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    queryset = Tour.objects.all()
    serializer_class = TourSerializers


class CreatedTours(ListAPIView):
    serializer_class = TourSerializers
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def get_queryset(self):
        return Tour.objects.filter(creator=self.request.user)


class Register(GenericAPIView):
    serializer_class = TourSerializers
    permission_classes = [permissions.AllowAny]

    def post(self, request):
        if not (request.user and self.request.user.is_authenticated):
            return Response(status=401, data={"error": "Invalid user"})

        if 'tour_id' not in request.data:
            return Response(status=400, data={"error": "Invalid tour"})

        if not Tour.objects.filter(pk=request.data['tour_id']).exists():
            return Response(status=400, data={"error": "Invalid tour"})

        registered_tour = Tour.objects.get(pk=request.data['tour_id'])
        request.user.pending_registered_tours.add(registered_tour)
        registered_tour.pending_users.add(request.user)
        return Response(status=200)


# TODO: fix


class MyConfirmedTours(ListAPIView):
    serializer_class = TourSerializers

    def get_queryset(self):
        return self.request.user.confirmed_registered_tours


class MyPendingTours(ListAPIView):
    serializer_class = TourSerializers

    def get_queryset(self):
        return self.request.user.pending_registered_tours


class MyPendingUsers(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    serializer_class = UserInfoSerializer

    def get(self, request):
        return_data = {}
        for tour in Tour.objects.filter(creator=request.user):
            serializer = UserInfoSerializer(tour.pending_users.all(), many=True)
            return_data[tour.tour_id] = serializer.data
        return Response(return_data)


class MyConfirmedUsers(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    serializer_class = UserInfoSerializer

    def get(self, request):
        return_data = {}
        for tour in Tour.objects.filter(creator=request.user):
            serializer = UserInfoSerializer(tour.confirmed_users.all(), many=True)
            return_data[tour.tour_id] = serializer.data
        return Response(return_data)

class AcceptUser(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    serializer_class = UserInfoSerializer

    def post(self, request):
        if not (request.user and self.request.user.is_authenticated):
            return Response(status=401, data={"error": "Invalid user"})

        if 'tour_id' not in request.data or not Tour.objects.filter(pk=request.data['tour_id']).exists():
            return Response(status=400, data={"error": "Invalid tour"})
         
        if 'user_id' not in request.data or not get_user_model().objects.filter(pk=request.data['user_id']).exists():
            return Response(status=400, data={"error": "Invalid user id"})

        if get_user_model().objects.get(pk=request.data['user_id']) not in Tour.objects.get(pk=request.data['tour_id']).pending_users:
            return Response(status=400, data={"error": "Invalid pending user"})

        if Tour.objects.get(pk=request.data['tour_id']) not in get_user_model().objects.get(pk=request.data['user_id']).pending_registered_tours:
            return Response(status=400, data={"error": "Invalid pending tour"})
        
        registerd_user = get_user_model().objects.get(pk=request.data['user_id'])
        registered_tour = Tour.objects.get(pk=request.data['tour_id'])
        registered_tour.confirmed_user.add(registerd_user)
        registered_tour.pending_users.pop(registerd_user)
        registerd_user.pending_registered_tours.pop(registered_tour)
        registerd_user.confirmed_registered_tours.add(registered_tour)
        return Response(status=200)




class AddTour(GenericAPIView):
    serializer_class = TourSerializers
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def post(self, request):
        if not (request.user and self.request.user.is_authenticated and self.request.user.is_tour_leader):
            return Response(status=401, data={"error": "Invalid user"})

        serializer = TourCreatSerializer(data=request.data)
        if serializer.is_valid():
            new_tour = Tour.objects.create(**serializer.data, creator_id=request.user.user_id)
            for id in request.data['places']:
                #place = Place.objects.get(pk=id)
                new_tour.places.add(id)
            return Response(status=200, data={"Tour added successfully."})
        else:
            return Response(status=400, data={"Unable to add tour."})

