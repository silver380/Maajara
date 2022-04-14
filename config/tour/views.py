from rest_framework.generics import ListAPIView
from .models import Tour
from .serializers import TourSerializers

# Create your views here.
class TourListAPIView(ListAPIView):
	queryset = Tour.objects.all()
	serializer_class = TourSerializers