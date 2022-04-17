from rest_framework import serializers
from django.contrib.auth import get_user_model


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ("email", "password", "first_name", "last_name")

    email = serializers.EmailField(max_length=255, required=True)
    first_name = serializers.CharField(max_length=50, required=True)
    last_name = serializers.CharField(max_length=50, required=True)
    password = serializers.CharField(write_only=True, required=True)

    def create(self, validated_data):
        user = get_user_model().objects.create_user(**validated_data)
        return user

    def validate_email(self, value):
        if get_user_model().objects.filter(email=value.lower()).exists():
            raise serializers.ValidationError("email exists.")
        return value


class UserUpgradeSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        fields = ("date_of_birth", "gender", "biography", "languages", "phone_number", "telegram_id", "whatsapp_id")

    date_of_birth = serializers.DateField(required=True)
    gender = serializers.ChoiceField(choices=[('Female', 'F'), ('Male', 'M')], required=True)
    biography = serializers.CharField(max_length=1003, required=True)
    languages = serializers.CharField(max_length=500, required=True)
    phone_number = serializers.CharField(max_length=50, required=True)
    telegram_id = serializers.CharField(max_length=50, required=False)
    whatsapp_id = serializers.CharField(max_length=50, required=False)


class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = get_user_model()
        exclude = ('pending_registered_tours', 'confirmed_registered_tours', 'is_admin',
                   'is_tour_leader', 'last_login', 'password')
