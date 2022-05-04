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
        for travel_plan in TravelPlan.objects.filter(plan_creator = request.user):
            data = []
            for req in TravelPlanReq.objects.filter(travel_plan_id = travel_plan.travel_plan_id):
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
            serialized_data = TravelPlanSerializer(req.travel_plan_id)
            return_data.append(serialized_data.data)
            
        return Response(return_data)

class AcceptTourLeader(GenericAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = UserInfoSerializer

    def post(self, request):
        pass
        # if 'tour_id' not in request.data or not Tour.objects.filter(pk=request.data['tour_id']).exists():
        #     return Response(status=400, data={"error": "Invalid tour"})

        # if 'user_id' not in request.data or not get_user_model().objects.filter(pk=request.data['user_id']).exists():
        #     return Response(status=400, data={"error": "Invalid user id"})

        # registered_user = get_user_model().objects.get(pk=request.data['user_id'])
        # registered_tour = Tour.objects.get(pk=request.data['tour_id'])

        # if registered_user not in registered_tour.pending_users.all():
        #     return Response(status=400, data={"error": "Invalid pending user"})

        # if registered_tour.creator != request.user:
        #     return Response(status=401, data={"error": "invalid tour leader"})

        # registered_tour.confirmed_users.add(registered_user)
        # registered_tour.pending_users.remove(registered_user)
        # return Response(status=200)

