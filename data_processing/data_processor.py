import pandas as pd
from geopy.exc import GeocoderTimedOut
from geopy.geocoders import Nominatim

# Load the CSV file
df = pd.read_csv('/home/davide/Desktop/physioadmissions/data_processing/raw_data/studentsRaw.csv', delimiter=';')

# Initialize the geolocator
geolocator = Nominatim(user_agent="geoapiExercises")


def get_location_by_address(address):
    """This function returns a location as raw from an address
    will repeat until success"""
    try:
        location = geolocator.geocode(address)
        return location.latitude, location.longitude
    except GeocoderTimedOut:
        return get_location_by_address(address)


# Apply the function to the address column
df['Address'] = df['Address'].apply(lambda x: get_location_by_address(x))

# Save the DataFrame to a new CSV file
df.to_csv('/home/davide/Desktop/physioadmissions/data_processing/processed_data/studentsProcessed.csv', sep=';', index=False)
