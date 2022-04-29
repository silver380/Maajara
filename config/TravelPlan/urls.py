from django.urls import path
from .views import TravelPlanListAPIView, CreatedTravelPlans, AddPlan
    

app_name = 'TravelPlan'
urlpatterns = [
    path('all/', TravelPlanListAPIView.as_view(), name="all_travel_palns"),
    path('createdplans/', CreatedTravelPlans.as_view(), name="created_travel_palns"),
    path('addplan/', AddPlan.as_view(), name="add_plan"), 

]
