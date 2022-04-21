from telnetlib import STATUS
from urllib import response
from rest_framework.generics import ListAPIView, GenericAPIView
from rest_framework.response import Response
from .serializers import TourSerializers, UserInfoSerializer
from rest_framework import permissions
from .permissions import IsTourLeader
from .models import Tour


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

        if 'id' not in request.data:
            return Response(status=400, data={"error": "Invalid tour"})

        if not Tour.objects.get(pk=request.data['id']):
            return Response(status=400, data={"error": "Invalid tour"})
       
        registered_tour = Tour.objects.get(pk=request.data['id'])
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
        Return_Data = {}
        for tour in Tour.objects.filter(creator=request.user):
            Return_pending_users = []
            for user in tour.pending_users.all():
                print(user)
                serializer = UserInfoSerializer(data=user)
                if serializer.is_valid():
                    Return_pending_users.append(serializer.data)
                # else:
                #     return Response(status=400, data={"Invalid User"})
            Return_Data[tour.id] = Return_pending_users
        return Response(Return_Data)
            

