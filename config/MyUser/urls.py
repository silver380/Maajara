from django.urls import path, include
from rest_framework.authtoken.views import obtain_auth_token
from .views import RegisterUsers, UpgradeToTL, UserInfo

urlpatterns = [
    path('token/', obtain_auth_token),
    path('register/', RegisterUsers.as_view()),
    path('upgrade/', UpgradeToTL.as_view()),
    path('info/', UserInfo.as_view()),
]
