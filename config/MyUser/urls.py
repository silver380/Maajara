from django.urls import path, include
from rest_framework.authtoken.views import obtain_auth_token
from .views import RegisterUsers, UpgradeToTL

urlpatterns = [
    path('token/', obtain_auth_token),
    path('register/', RegisterUsers.as_view()),
    path('upgrade/', UpgradeToTL.as_view()),
]
