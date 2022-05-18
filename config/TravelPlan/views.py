from rest_framework import permissions
from rest_framework.generics import ListAPIView, GenericAPIView, CreateAPIView
from rest_framework.response import Response
from rest_framework import filters

from django.shortcuts import get_object_or_404
from .models import TravelPlan, TravelPlanReq
from .permissions import IsTourLeader
from .serializers import TravelPlanSerializer, UserInfoSerializer, TravelPlanReqSerializer
from django.contrib.auth import get_user_model



class TravelPlanListAPIView(ListAPIView):
    search_fields = ['places','destination']
    filter_backends = (filters.SearchFilter,)
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]
    queryset = TravelPlan.objects.all()
    serializer_class = TravelPlanSerializer


class CreatedTravelPlans(ListAPIView):
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return TravelPlan.objects.filter(plan_creator=self.request.user)


class AddPlan(CreateAPIView):
    model = TravelPlan
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]


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
            serialized_data = TravelPlanSerializer(req.travel_plan)
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

