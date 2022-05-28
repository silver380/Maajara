from rest_framework import permissions
from rest_framework.generics import ListAPIView, GenericAPIView, CreateAPIView
from rest_framework.response import Response
from rest_framework import filters
import json

from django.shortcuts import get_object_or_404
from .models import TravelPlan, TravelPlanReq
from .permissions import IsTourLeader
from .serializers import TravelPlanSerializer, TravelPlanReqSerializer, AddTravelPlanSerializer
from MyUser.serializers import UserInfoSerializer
from django.contrib.auth import get_user_model


class TravelPlanListAPIView(ListAPIView):
    search_fields = ['places__name', 'destination']
    filter_backends = (filters.SearchFilter,)
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    queryset = TravelPlan.objects.active()
    serializer_class = TravelPlanSerializer


class CreatedTravelPlans(ListAPIView):
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return TravelPlan.objects.filter(plan_creator=self.request.user)


class AddPlan(CreateAPIView):
    model = TravelPlan
    serializer_class = AddTravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def perform_create(self, serializer):
        return serializer.save()

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        instance = self.perform_create(serializer)
        instance_serializer = TravelPlanSerializer(instance)
        return Response(instance_serializer.data)


class AddPlanReq(CreateAPIView):
    model = TravelPlanReq
    serializer_class = TravelPlanReqSerializer
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]


class MyPendingReqs(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request):
        return_data = {}
        for travel_plan in TravelPlan.objects.filter(plan_creator=request.user):
            data = []
            for req in TravelPlanReq.objects.filter(travel_plan_id=travel_plan.travel_plan_id):
                serialized_data = TravelPlanReqSerializer(req)
                data.append(serialized_data.data)
            return_data[travel_plan.travel_plan_id] = data

        return Response(return_data)


class MyPendingTravelPlans(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def get(self, request):
        travel_plans_reqs = TravelPlanReq.objects.filter(tour_leader=request.user)
        return_data = []
        for req in travel_plans_reqs:
            travel_plan = TravelPlanSerializer(req.travel_plan).data

            if (travel_plan['confirmed_tour_leader'] == None):
                serialized_data = TravelPlanReqSerializer(req)
                return_data.append(serialized_data.data)

        return Response(return_data)


class MyConfirmedTravelPlans(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

    def get(self, request):
        travel_plans_reqs = TravelPlanReq.objects.filter(tour_leader=request.user)
        return_data = []
        for req in travel_plans_reqs:
            travel_plan = json.loads(json.dumps(TravelPlanSerializer(req.travel_plan).data))
            confirmed_tour_leader = (travel_plan.get('confirmed_tour_leader'))
            if confirmed_tour_leader != None:
                confirmed_tour_leader_id = confirmed_tour_leader['user_id']
            else:
                confirmed_tour_leader_id = -1

            if (confirmed_tour_leader_id == request.user.user_id):
                serialized_data = TravelPlanReqSerializer(req)
                return_data.append(serialized_data.data)

        return Response(return_data)


class AcceptTourLeader(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = UserInfoSerializer

    def post(self, request):

        # get a user_id travel_plan_id
        # print(request.data)
        if 'travel_plan_id' not in request.data or 'user_id' not in request.data:
            return Response(status=401, data={"error": "invalid data"})

        travel_plan = get_object_or_404(TravelPlan, pk=request.data['travel_plan_id'])
        if travel_plan.confirmed_tour_leader != None:
            return Response(status=401, data={"error": "already confirmed Tour leader"})

        tour_leader = get_object_or_404(get_user_model(), pk=request.data['user_id'])
        travel_plan.confirmed_tour_leader = tour_leader
        travel_plan_req = TravelPlanReq.objects.filter(tour_leader=request.data['user_id'],
                                                       travel_plan_id=request.data['travel_plan_id'])[0]
        travel_plan.accepted_price = travel_plan_req.suggested_price
        travel_plan.save()
        return Response(status=200)
