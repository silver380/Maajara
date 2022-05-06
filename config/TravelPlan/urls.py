from django.urls import path

#from config.TravelPlan.models import TravelPlanReq
from .views import TravelPlanListAPIView, CreatedTravelPlans, AddPlan, AddPlanReq, MyPendingReqs, MyPendingTravelPlans, AcceptTourLeader
    

app_name = 'TravelPlan'
urlpatterns = [
    path('all/', TravelPlanListAPIView.as_view(), name="all_travel_plans"),
    path('createdplans/', CreatedTravelPlans.as_view(), name="created_travel_plans"),
    path('addplan/', AddPlan.as_view(), name="add_plan"), 
    path('addplanreq/', AddPlanReq.as_view(), name="addplanreq"),
    path('mypendingreqs/',MyPendingReqs.as_view(), name="mypendingreqs"),
    path('mypendingplans/',MyPendingTravelPlans.as_view(), name="mypendingplans"),
    path('accepttourleader/',AcceptTourLeader.as_view(), name="accepttourleader"),

]
