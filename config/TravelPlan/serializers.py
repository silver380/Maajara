from rest_framework import serializers
from .models import TravelPlan
from django.contrib.auth import get_user_model



class CreatorSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ['first_name', 'last_name', 'user_id']


class TravelPlanSerializer(serializers.ModelSerializer):
    creator = CreatorSerializer(read_only=True)

    class Meta:
        model = TravelPlan
        exclude = ('pending_leaders', ) # + 'confirmed_leade'

    paln_name = serializers.CharField(max_length=60, required=True)
    destination = serializers.CharField(max_length=60, required=True)
    start_date = serializers.DateField(required=True)
    end_date = serializers.DateField(required=True)
    wanted_list = serializers.CharField() 
    
