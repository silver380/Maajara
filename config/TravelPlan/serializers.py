from rest_framework import serializers
from .models import TravelPlan, TravelPlanReq
from django.contrib.auth import get_user_model
from Place.serializers import PlaceSerializers
from MyUser.serializers import TourLeaderSerializer, UserSerializer, UserInfoSerializer


class AddTravelPlanSerializer(serializers.ModelSerializer):
    plan_creator = UserSerializer(read_only=True)
    confirmed_tour_leader = TourLeaderSerializer(read_only=True)

    class Meta:
        model = TravelPlan
        exclude = ('pending_leaders',)

    destination = serializers.CharField(max_length=60, required=True)
    start_date = serializers.DateField(required=True)
    end_date = serializers.DateField(required=True)
    wanted_list = serializers.CharField()

    def create(self, validated_data):
        id_place = validated_data.pop('places')
        travel_plan = TravelPlan.objects.create(**validated_data, plan_creator=self.context['request'].user)
        travel_plan.places.add(*id_place)
        return travel_plan

    def validate(self, data):
        if data['start_date'] > data['end_date']:
            raise serializers.ValidationError({"end_date": "Finish must occur after start"})
        return data


class TravelPlanSerializer(serializers.ModelSerializer):
    plan_creator = UserSerializer(read_only=True)
    confirmed_tour_leader = TourLeaderSerializer(read_only=True)
    places = PlaceSerializers(many=True, required=False)

    class Meta:
        model = TravelPlan
        exclude = ('pending_leaders',)


class TravelPlanReqSerializer(serializers.ModelSerializer):
    tour_leader = TourLeaderSerializer(read_only=True)
    travel_plan = TravelPlanSerializer(read_only=True)

    class Meta:
        model = TravelPlanReq
        fields = '__all__'

    def create(self, validated_data):
        plan_req = TravelPlanReq.objects.create(**validated_data, tour_leader=self.context['request'].user,
                                                travel_plan_id=self.context['request'].data['travel_plan_id'])

        self.context['request'].user.decrease_ticket()
        return plan_req
