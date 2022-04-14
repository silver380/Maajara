from rest_framework import serializers
from tour.models import Tour
class TourSerializers(serializers.ModelSerializer):
		class Meta:
			model = Tour
			fields ='__all__' # it should change