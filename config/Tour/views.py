from django.db.models import OuterRef, Avg
from rest_framework import filters
from rest_framework import permissions
from rest_framework.generics import ListAPIView, CreateAPIView
from rest_framework.response import Response
from rest_framework.views import APIView

from .permissions import *
from .serializers import *
import datetime


class TourListAPIView(ListAPIView):
    search_fields = ['tour_name', 'destination', 'places__name']
    filter_backends = (filters.SearchFilter,)
    permission_classes = [permissions.IsAuthenticated]
    queryset = Tour.objects.active()
    serializer_class = TourListSerializer


class CreatedTours(ListAPIView):
    serializer_class = TourListSerializer
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def get_queryset(self):
        return Tour.objects.active_or_not_ended().filter(creator=self.request.user)


class Register(APIView):
    serializer_class = TourListSerializer
    permission_classes = [permissions.AllowAny]

    def post(self, request):
        if 'tour_id' not in request.data:
            return Response(status=400, data={"error": "Invalid tour"})

        registered_tour = get_object_or_404(Tour, pk=request.data['tour_id'])

        if registered_tour.is_full:
            return Response(status=400, data={"error": "Tour is full."})

        current_date = datetime.date.today()
        if registered_tour.start_date < current_date:
            return Response(status=400, data={"error": "Cant register past tours."})

        registered_tour.pending_users.add(request.user)
        return Response(status=200)


class MyConfirmedTours(ListAPIView):
    serializer_class = TourListSerializer

    def get_queryset(self):
        now = datetime.date.today()
        return self.request.user.confirmed_tours.filter(end_date__gte=now)


class MyPendingTours(ListAPIView):
    serializer_class = TourListSerializer

    def get_queryset(self):
        now = datetime.datetime.now()
        return self.request.user.pending_tours.filter(start_date__gte=now)


class MyPendingUsers(APIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    serializer_class = UserInfoSerializer

    def get(self, request):
        return_data = {}
        for tour in Tour.objects.filter(creator=request.user):
            now = datetime.date.today()
            if tour.end_date < now:
                continue

            serializer = UserInfoSerializer(tour.pending_users.all(), many=True)
            return_data[tour.tour_id] = serializer.data
        return Response(return_data)


class MyConfirmedUsers(APIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def get(self, request):
        return_data = {}
        for tour in Tour.objects.filter(creator=request.user):
            now = datetime.date.today()
            if tour.end_date < now:
                continue
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

        if registered_tour.is_full:
            return Response(status=401, data={"error": "Tour is full."})

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
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        instance = self.perform_create(serializer)
        instance_serializer = TourListSerializer(instance)
        return Response(instance_serializer.data)


class AddRate(CreateAPIView):
    model = TourRate
    serializer_class = TourRateSerializer
    permission_classes = [permissions.IsAuthenticated and CanRate]


class GetRate(APIView):
    serializer_class = TourRateSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, tour_id):
        # TODO: date set
        return_data = {}
        current_tour = get_object_or_404(Tour, pk=tour_id)
        current_date = datetime.date.today()
        can_rate = (current_tour.end_date < current_date) and (request.user in current_tour.confirmed_users.all())
        try:
            current_rate = TourRate.objects.filter(user_id=request.user.user_id, tour_id=tour_id).values()[0]
        except Exception:
            current_rate = None

        return_data['current_rate'] = TourRateSerializer(current_rate).data
        return_data['can_rate'] = can_rate
        return Response(return_data)


class TourSuggestion(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, pk):
        tour = get_object_or_404(Tour, pk=pk)
        tour_places = tour.places.all()
        tour_places = tour_places.reverse()

        try:
            latitude_avg = sum([x.latitude for x in tour_places]) / len(tour_places)
            longitude_avg = sum([x.longitude for x in tour_places]) / len(tour_places)
        except ZeroDivisionError:
            latitude_avg = 32
            longitude_avg = 53

        # places_with_distance = Place.objects.annotate(
        #     distance=(tour_places.annotate(
        #         distance_to_place_in_set=(
        #             Min(
        #                 (F('latitude') - OuterRef('latitude')) * (F('latitude') - OuterRef('latitude')) +
        #                 (F('longitude') - OuterRef('longitude')) * (F('longitude') - OuterRef('longitude'))
        #             )
        #         )
        #     ).values('distance_to_place_in_set')))

        places_with_distance = Place.objects.annotate(distance=(
                ((F('latitude') - latitude_avg) * (F('latitude') - latitude_avg)) +
                ((F('longitude') - longitude_avg) * (F('longitude') - longitude_avg))
        )).order_by('distance')

        closest = Tour.objects.active().exclude(pk=tour.pk).annotate(avg_distance=Avg(places_with_distance.filter(
            place_id__in=OuterRef('places')).values('distance'))).order_by('avg_distance')

        serialized = TourListSerializer(closest[:5], many=True)
        return Response(serialized.data)

class ArchivedTourTL(ListAPIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    serializer_class = TourListSerializer

    def get_queryset(self):
        return Tour.objects.inactive().filter(creator=self.request.user)

class ArchivedTourUser(ListAPIView):

    permission_classes = [permissions.IsAuthenticated]
    serializer_class = TourListSerializer

    def get_queryset(self):

        return self.request.user.confirmed_tours.inactive()
        
