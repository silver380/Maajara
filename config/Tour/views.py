from django.shortcuts import get_object_or_404
from rest_framework.views import APIView

from .serializers import *
from rest_framework.generics import ListAPIView, GenericAPIView, CreateAPIView
from django.contrib.auth import get_user_model
from rest_framework.response import Response
from rest_framework import permissions
from rest_framework import filters
from .permissions import *
from .models import *
import datetime


class TourListAPIView(ListAPIView):
    search_fields = ['tour_name','destination','places__name']
    filter_backends = (filters.SearchFilter,)
    permission_classes = [permissions.IsAuthenticated]
    queryset = Tour.objects.all()
    serializer_class = TourListSerializer


class CreatedTours(ListAPIView):
    serializer_class = TourListSerializer
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def get_queryset(self):
        return Tour.objects.filter(creator=self.request.user)


class Register(APIView):
    serializer_class = TourListSerializer
    permission_classes = [permissions.AllowAny]

    def post(self, request):
        if 'tour_id' not in request.data:
            return Response(status=400, data={"error": "Invalid tour"})

        registered_tour = get_object_or_404(Tour, pk=request.data['tour_id'])
        registered_tour.pending_users.add(request.user)
        return Response(status=200)


class MyConfirmedTours(ListAPIView):
    serializer_class = TourListSerializer

    def get_queryset(self):
        return self.request.user.confirmed_tours


class MyPendingTours(ListAPIView):
    serializer_class = TourListSerializer

    def get_queryset(self):
        return self.request.user.pending_tours


class MyPendingUsers(APIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    serializer_class = UserInfoSerializer

    def get(self, request):
        return_data = {}
        for tour in Tour.objects.filter(creator=request.user):
            serializer = UserInfoSerializer(tour.pending_users.all(), many=True)
            return_data[tour.tour_id] = serializer.data
        return Response(return_data)


class MyConfirmedUsers(APIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def get(self, request):
        return_data = {}
        for tour in Tour.objects.filter(creator=request.user):
            serializer = UserInfoSerializer(tour.confirmed_users.all(), many=True)
            return_data[tour.tour_id] = serializer.data
        return Response(return_data)


class AcceptUser(APIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def post(self, request):
        if 'tour_id' not in request.data or 'user_id' not in request.data:
            return Response(status=400, data={"error": "Invalid data"})

        registered_user = get_object_or_404(get_user_model(), pk=request.data['user_id'])
        registered_tour = get_object_or_404(Tour, pk=request.data['tour_id'])

        if registered_user not in registered_tour.pending_users.all():
            return Response(status=400, data={"error": "Invalid pending user"})

        if registered_tour.creator != request.user:
            return Response(status=401, data={"error": "invalid tour leader"})

        registered_tour.confirmed_users.add(registered_user)
        registered_tour.pending_users.remove(registered_user)
        return Response(status=200)


class Add(CreateAPIView):
    model = Tour
    serializer_class = AddTourSerializers
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def perform_create(self, serializer):
        return serializer.save()

    def create(self, request, *args, **kwargs):
        request.user.decrease_ticket()
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        instance = self.perform_create(serializer)
        instance_serializer = TourListSerializer(instance)
        return Response(instance_serializer.data)


class AddRate(CreateAPIView):
    model = TourRate
    serializer_class = TourRateSerializer
    permission_classes = [permissions.IsAuthenticated  and CanRate]


class GetRate(APIView):

    serializer_class = TourRateSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, tour_id):

        return_data = {}
        current_tour = get_object_or_404(Tour, pk=tour_id)
        current_date = datetime.date.today() 
        can_rate =  (current_tour.end_date < current_date) and (request.user in current_tour.confirmed_users.all())
        try:
            current_rate = TourRate.objects.filter(user_id= request.user.user_id, tour_id = tour_id).values()[0]
        except Exception as e:
                current_rate = None

        return_data['current_rate'] =  TourRateSerializer(current_rate).data
        return_data['can_rate'] = can_rate
        return Response(return_data)
