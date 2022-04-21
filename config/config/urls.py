from django.contrib import admin
from django.urls import path, include
from django.conf.urls.static import static
from django.conf import settings


urlpatterns = [
    path('admin/', admin.site.urls),
    path('auth/', include('MyUser.urls')),
    path('tour/', include('Tour.urls')),
    path('place/', include('Place.urls')),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
