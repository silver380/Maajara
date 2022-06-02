from rest_framework.permissions import BasePermission


class IsTourLeader(BasePermission):
    def has_permission(self, request, view):
        return request.user and request.user.is_authenticated and request.user.is_tour_leader
