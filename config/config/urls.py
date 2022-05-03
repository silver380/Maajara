from django.contrib import admin

from django.urls import path, include, re_path
from MyUser.views import place_picture

urlpatterns = [
    path('admin/', admin.site.urls),
    path('auth/', include('MyUser.urls')),
    path('tour/', include('Tour.urls')),
    path('place/', include('Place.urls')),
    path('travelplan/', include('TravelPlan.urls')),
    re_path(r'^media/(?P<folder>.*)/(?P<file_name>.*)$', place_picture),
]
