from .serializers import TourCreatSerializer, UserInfoSerializer, TourSerializers
from rest_framework.generics import ListAPIView, GenericAPIView, CreateAPIView
from django.contrib.auth import get_user_model
from rest_framework.response import Response
from rest_framework import permissions
from .permissions import IsTourLeader
from .models import Tour


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
        if 'tour_id' not in request.data:
            return Response(status=400, data={"error": "Invalid tour"})

        if not Tour.objects.filter(pk=request.data['tour_id']).exists():
            return Response(status=400, data={"error": "Invalid tour"})

        registered_tour = Tour.objects.get(pk=request.data['tour_id'])
        registered_tour.pending_users.add(request.user)
        return Response(status=200)


class MyConfirmedTours(ListAPIView):
    serializer_class = TourSerializers

    def get_queryset(self):
        return self.request.user.confirmed_tours


class MyPendingTours(ListAPIView):
    serializer_class = TourSerializers

    def get_queryset(self):
        return self.request.user.pending_tours


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
        if 'tour_id' not in request.data or not Tour.objects.filter(pk=request.data['tour_id']).exists():
            return Response(status=400, data={"error": "Invalid tour"})

        if 'user_id' not in request.data or not get_user_model().objects.filter(pk=request.data['user_id']).exists():
            return Response(status=400, data={"error": "Invalid user id"})

        registered_user = get_user_model().objects.get(pk=request.data['user_id'])
        registered_tour = Tour.objects.get(pk=request.data['tour_id'])

        if registered_user not in registered_tour.pending_users.all():
            return Response(status=400, data={"error": "Invalid pending user"})

        if registered_tour.creator != request.user:
            return Response(status=401, data={"error": "invalid tour leader"})

        registered_tour.confirmed_users.add(registered_user)
        registered_tour.pending_users.remove(registered_user)
        return Response(status=200)


class AddTour(GenericAPIView):
    serializer_class = TourSerializers
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def post(self, request):
        id_place = request.data['places']
        data = request.data.copy()
        data.pop('places')
        serializer = TourCreatSerializer(data=data)
        if serializer.is_valid():
            new_tour = Tour.objects.create(**serializer.data, creator_id=request.user.user_id)
            try:
                for p_id in id_place:
                    new_tour.places.add(p_id)
            except Exception as e:
                print(e)

            return Response(status=200, data={"detail": "Tour added successfully.", "data": request.data})
        else:
            return Response(status=400, data={"error": serializer.errors})

class Add(CreateAPIView):
    model = Tour
    serializer_class = TourSerializers
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
