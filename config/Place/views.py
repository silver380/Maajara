from django.http import FileResponse
from rest_framework import permissions
from rest_framework.generics import ListAPIView
from rest_framework.views import APIView
from config.settings import MEDIA_ROOT
from .serializers import PlaceSerializers
from .models import Place


class PlaceListView(ListAPIView):
    queryset = Place.objects.all()
    serializer_class = PlaceSerializers


class PlacePicture(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, file_name, folder):
        absolute_path = MEDIA_ROOT + '/' + folder + '/' + file_name
        response = FileResponse(open(absolute_path, 'rb'), as_attachment=True)
        return response
