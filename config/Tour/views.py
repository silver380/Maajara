from rest_framework.generics import ListAPIView, GenericAPIView
from rest_framework.response import Response
from .serializers import TourSerializers
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

        request.user.pending_registered_tours.add(Tour.objects.get(pk=request.data['id']))
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

