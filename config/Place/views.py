from rest_framework.generics import ListCreateAPIView
from .models import Place
from .serializers import PlaceSerializers





class PlaceListView(ListCreateAPIView):
    # todo: add permission classes
    queryset = Place.objects.all()
    serializer_class = PlaceSerializers


