from rest_framework.generics import ListCreateAPIView
from rest_framework import filters
from .models import Place
from .serializers import PlaceSerializers





class PlaceListView(ListCreateAPIView):
    # todo: add permission classes
    search_fields = ['name','city']
    filter_backends = (filters.SearchFilter,)
    queryset = Place.objects.all()
    serializer_class = PlaceSerializers


