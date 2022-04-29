from django.shortcuts import render
from .serializers import CreatorSerializer, TravelPlanSerializer, TravelPlanCreatSerializer
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



