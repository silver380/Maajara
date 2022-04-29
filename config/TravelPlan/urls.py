from django.urls import path
from .views import TravelPlanListAPIView, CreatedTravelPlans, AddPlan, Register, MyPendingLeaders
    

app_name = 'TravelPlan'
urlpatterns = [
    path('all/', TravelPlanListAPIView.as_view(), name="all_travel_palns"),
    path('register/', Register.as_view(), name="Register"),
    path('createdplans/', CreatedTravelPlans.as_view(), name="created_travel_palns"),
    path('addplan/', AddPlan.as_view(), name="add_plan"), 
    path('pendingleaders/', MyPendingLeaders.as_view(), name="pendig_leaders"),

]
