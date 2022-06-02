from rest_framework.generics import ListCreateAPIView
from rest_framework import filters, permissions
from .models import Place
from .serializers import PlaceSerializers


class PlaceListView(ListCreateAPIView):
    permission_classes = [permissions.IsAuthenticated]
    search_fields = ['name', 'city']
    filter_backends = (filters.SearchFilter,)
    queryset = Place.objects.active()
    serializer_class = PlaceSerializers
