from django.urls import path, re_path
from rest_framework.authtoken.views import obtain_auth_token
from .views import RegisterUsers, UserInfo, ActivateUser, UserUpdate, IncreaseTicket


urlpatterns = [
    path('token/', obtain_auth_token),
    path('register/', RegisterUsers.as_view()),
    path('upgrade/', UserUpdate.as_view()),
    path('info/', UserInfo.as_view()),
    path('increaseticket/',IncreaseTicket.as_view()),
    re_path(r'^activate/(?P<uid>[\w-]+)/(?P<token>[\w-]+)/$', ActivateUser.as_view()),
]
