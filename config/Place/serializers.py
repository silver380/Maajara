from rest_framework import serializers
from .models import Place


class PlaceSerializers(serializers.ModelSerializer):
    class Meta:
        model = Place
        fields = '__all__'  # it should change
