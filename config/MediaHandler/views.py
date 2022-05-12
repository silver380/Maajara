from django.http import FileResponse
from rest_framework.exceptions import NotFound
from rest_framework import permissions
from rest_framework.views import APIView
from config.settings import MEDIA_ROOT


class GetFile(APIView):
    permission_classes = [permissions.IsAuthenticated]

    def get(self, request, file_name, folder):
        try:
            absolute_path = MEDIA_ROOT + '/' + folder + '/' + file_name
            response = FileResponse(open(absolute_path, 'rb'), as_attachment=True)
        except Exception as e:
            response = NotFound('File does not exist')
        return response


