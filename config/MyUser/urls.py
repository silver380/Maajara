from django.urls import path, re_path
from rest_framework.authtoken.views import obtain_auth_token
from .views import RegisterUsers, UpgradeToTL, UserInfo, ActivateUser


urlpatterns = [
    path('token/', obtain_auth_token),
    path('register/', RegisterUsers.as_view()),
    path('upgrade/', UpgradeToTL.as_view()),
    path('info/', UserInfo.as_view()),
    re_path(r'^activate/(?P<uid>[\w-]+)/(?P<token>[\w-]+)/$', ActivateUser.as_view()),
]
