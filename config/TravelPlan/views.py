from django.shortcuts import render
from .serializers import CreatorSerializer, TravelPlanSerializer
from rest_framework.generics import ListAPIView, GenericAPIView
from django.contrib.auth import get_user_model
from rest_framework.response import Response
from rest_framework import permissions
from .permissions import IsTourLeader
from .models import TravelPlan


class TravelPlanListAPIView(ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    queryset = TravelPlan.objects.all()
    serializer_class = TravelPlanSerializer

class CreatedTravelPlans(ListAPIView):
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return TravelPlan.objects.filter(plan_creator=self.request.user)
