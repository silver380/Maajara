from rest_framework import serializers
from .models import Tour
from django.contrib.auth import get_user_model


class TourSerializers(serializers.ModelSerializer):
    class Meta:
        model = Tour
        fields = '__all__'  # it should change

class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        # exclude = ('pending_registered_tours', 'confirmed_registered_tours', 'is_admin',
        #            'last_login', 'password')
        fields = '__all__'  # it should change
