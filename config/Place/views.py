from rest_framework.generics import ListAPIView
from .serializers import PlaceSerializers
from .models import Place


class PlaceListView(ListAPIView):
    queryset = Place.objects.all()
    serializer_class = PlaceSerializers
