from rest_framework import serializers
from django.contrib.auth import get_user_model


class UserSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)

    def create(self, validated_data):
        user = get_user_model().objects.create_user(
            **validated_data
        )

        return user

    class Meta:
        model = get_user_model()
        fields = ("email", "password", "first_name", "last_name")
