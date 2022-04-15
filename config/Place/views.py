from rest_framework.generics import ListAPIView
from django.shortcuts import render
from .models import Place
from .serializers import PlaceSerializers
# Create your views here.

class PlaceListView(ListAPIView):
    queryset = Place.objects.all()
    serializer_class = PlaceSerializers