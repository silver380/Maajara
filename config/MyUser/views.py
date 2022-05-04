from django.contrib.auth import get_user_model
from django.http import HttpResponseForbidden, FileResponse
from rest_framework import permissions
from rest_framework.generics import CreateAPIView, GenericAPIView, RetrieveAPIView
from rest_framework.views import APIView
from rest_framework.response import Response
from config.settings import MEDIA_ROOT
from .serializers import UserSerializer, UserUpgradeSerializer, UserInfoSerializer


class RegisterUsers(CreateAPIView):
    model = get_user_model()
    permission_classes = [
        permissions.AllowAny
    ]
    serializer_class = UserSerializer


class UpgradeToTL(GenericAPIView):
    permission_classes = [
        permissions.IsAuthenticated
    ]

    def post(self, request):
        if not (request.user and self.request.user.is_authenticated):
            return Response(status=401, data={"error": "Invalid user"})

        serializer = UserUpgradeSerializer(data=request.data)
        if serializer.is_valid():
            request.user.upgrade(serializer.data)
            return Response(status=200, data={"User upgraded successfully."})
        else:
            return Response(status=400, data={"error": serializer.errors})


class UserInfo(RetrieveAPIView):
    permission_classes = [
        permissions.IsAuthenticated
    ]
    serializer_class = UserInfoSerializer

    def get_object(self):
        return self.request.user


