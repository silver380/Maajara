from rest_framework import permissions
from rest_framework.generics import ListAPIView, GenericAPIView, CreateAPIView
from rest_framework.response import Response

from .models import TravelPlan, TravelPlanReq
from .permissions import IsTourLeader
from .serializers import TravelPlanSerializer, UserInfoSerializer, TravelPlanReqSerializer


class TravelPlanListAPIView(ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    queryset = TravelPlan.objects.all()
    serializer_class = TravelPlanSerializer


class CreatedTravelPlans(ListAPIView):
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return TravelPlan.objects.filter(plan_creator=self.request.user)


class Register(GenericAPIView):  # it should change > pending leaders
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


class MyPendingLeaders(GenericAPIView):  # it should change.
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = UserInfoSerializer

    def get(self, request):
        return_data = {}
        for plan in TravelPlan.objects.filter(plan_creator=request.user):
            serializer = UserInfoSerializer(plan.pending_leaders.all(), many=True)
            return_data[plan.plan_id] = serializer.data
        return Response(return_data)


class AddPlan(CreateAPIView):
    model = TravelPlan
    serializer_class = TravelPlanSerializer
    permission_classes = [permissions.IsAuthenticated]


class Register(GenericAPIView):  # it should change > pending leaders
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

class AddPlanReq(CreateAPIView):
    model = TravelPlanReq
    serializer_class = TravelPlanReqSerializer
    permission_classes = [permissions.IsAuthenticated and IsTourLeader]

class MyPendingReqs(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = UserInfoSerializer

    def get(self, request):
        return_data = {}
        for travel_plan in TravelPlan.objects.filter(plan_creator = request.user):
            for req in TravelPlanReq.objects.filter(travel_plan_id = travel_plan.travel_plan_id):
                serialized_data = TravelPlanReqSerializer(req)
                return_data[travel_plan.travel_plan_id].append(serialized_data)  
        return Response(return_data)        


