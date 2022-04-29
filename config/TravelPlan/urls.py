from django.urls import path
from .views import TravelPlanListAPIView, CreatedTravelPlans
    

app_name = 'TravelPlan'
urlpatterns = [
    path('all/', TravelPlanListAPIView.as_view(), name="all_travel_palns"),
    path('createdplans/', CreatedTravelPlans.as_view(), name="created_travel_palns"),

]
