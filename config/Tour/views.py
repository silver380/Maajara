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

        if 'tour_id' not in request.data:
            return Response(status=401, data={"error": "Invalid tour"})

        request.user.registered_tours.add(Tour.objects.get(pk=request.data['tour_id']))
        return Response(request.user.email)


