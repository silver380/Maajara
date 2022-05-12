from django.contrib import admin
from django.urls import path, include, re_path

from MediaHandler.views import GetFile

urlpatterns = [
    path('admin/', admin.site.urls),
    path('auth/', include('MyUser.urls')),
    path('tour/', include('Tour.urls')),
    path('place/', include('Place.urls')),
    path('travelplan/', include('TravelPlan.urls')),
    re_path(r'^auth/', include('djoser.urls')),
    re_path(r'^auth/', include('djoser.urls.authtoken')),
    re_path(r'^media/(?P<folder>.*)/(?P<file_name>.*)$', GetFile.as_view()),
]
