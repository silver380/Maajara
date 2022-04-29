from django.shortcuts import render
from .serializers import CreatorSerializer, TravelPlanSerializer, TravelPlanCreatSerializer, UserInfoSerializer
from rest_framework.generics import ListAPIView, GenericAPIView, CreateAPIView
from django.contrib.auth import get_user_model
from rest_framework.response import Response
from rest_framework import permissions
from .permissions import IsTourLeader
from .models import TravelPlan
from rest_framework.mixins import CreateModelMixin


class TravelPlanListAPIView(ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    queryset = TravelPlan.objects.all()
    serializer_class = TravelPlanSerializer

class CreatedTravelPlans(ListAPIView):
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return TravelPlan.objects.filter(plan_creator=self.request.user)

class Register(GenericAPIView): #it should change > pending leaders 
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated and IsTourLeader] 

    def post(self, request):
        if 'plan_id' not in request.data:
            return Response(status=400, data={"error": "Invalid travel plan"})

        if not TravelPlan.objects.filter(pk=request.data['plan_id']).exists():
            return Response(status=400, data={"error": "Invalid travel plan"})

        registered_plan = TravelPlan.objects.get(pk=request.data['plan_id'])
        registered_plan.pending_leaders.add(request.user)
        return Response(status=200)

class MyPendingLeaders(GenericAPIView): # it should change.
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = UserInfoSerializer

    def get(self, request):
        return_data = {}
        for plan in TravelPlan.objects.filter(plan_creator=request.user):
            serializer = UserInfoSerializer(plan.pending_leaders.all(), many=True)
            return_data[plan.plan_id] = serializer.data
        return Response(return_data)
class AddPlan(GenericAPIView):
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def post(self, request):    

        serializer = TravelPlanCreatSerializer(data=request.data)
        if serializer.is_valid():
            TravelPlan.objects.create(**serializer.data, plan_creator=request.user)

            return Response(status=200, data={"detail": "Travel plan added successfully.", "data": request.data})
        else:
            return Response(status=400, data={"error": serializer.errors})


