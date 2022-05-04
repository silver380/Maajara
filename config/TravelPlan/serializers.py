from rest_framework import serializers
from .models import TravelPlan, TravelPlanReq
from django.contrib.auth import get_user_model


class CreatorSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ['email', 'first_name', 'last_name', 'user_id']


class TravelPlanSerializer(serializers.ModelSerializer):
    plan_creator = CreatorSerializer(read_only=True)

    class Meta:
        model = TravelPlan
        exclude = ('pending_leaders',)  # + 'confirmed_leader'

    travel_plan_name = serializers.CharField(max_length=60, required=True)
    destination = serializers.CharField(max_length=60, required=True)
    start_date = serializers.DateField(required=True)
    end_date = serializers.DateField(required=True)
    wanted_list = serializers.CharField()

    def create(self, validated_data):
        plan = TravelPlan.objects.create(**validated_data, plan_creator=self.context['request'].user)
        return plan


class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        exclude = ('is_admin', 'last_login', 'password')




class TravelPlanReqSerializer(serializers.ModelSerializer):
    tour_leader = CreatorSerializer(read_only=True)

    class Meta:
        model = TravelPlanReq
        #exclude = ('tour_leader',)  # + 'confirmed_leade'
        fields = '__all__'

   

    def create(self, validated_data):
        plan_req = TravelPlanReq.objects.create(**validated_data, tour_leader=self.context['request'].user)
        return plan_req
