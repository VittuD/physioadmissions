# data_processor.py

import time
import pandas as pd
from geopy.exc import GeocoderTimedOut
from geopy.geocoders import Photon
from data_processing_functions import calculate_distance, add_preference_columns, get_sorted_students, fill_preferences

# Load the CSV file
students_df = pd.read_csv('/data_processing/converted_data/studentsRaw.csv',
                          delimiter=';')
colleges_df = pd.read_csv('/data_processing/converted_data/collegesRaw.csv',
                          delimiter=';')

# Initialize the geolocator
geolocator = Photon(user_agent="geoapiExercises")


def get_location_by_address(address):
    print(address)
    """This function returns a location as raw from an address
    will repeat until success"""
    try:
        location = geolocator.geocode(address)
        time.sleep(1)
        print(location)
        if location is not None:
            return location.latitude, location.longitude
        else:
            return None, None
    except GeocoderTimedOut:
        return get_location_by_address(address)


# Apply the function to the address column
if 'Address' in students_df.columns:
    students_df['Address'] = students_df['Address'].apply(lambda x: get_location_by_address(x))
else:
    print(students_df.columns)
    print("Column 'Address' does not exist in the students DataFrame.")

if 'Address' in colleges_df.columns:
    colleges_df['Address'] = colleges_df['Address'].apply(lambda x: get_location_by_address(x))
else:
    print(colleges_df.columns)
    print("Column 'Address' does not exist in the colleges DataFrame.")

# Save the DataFrame to a new CSV file
students_df.to_csv('/data_processing/processed_data/studentsProcessed.csv',
                   sep=';',
                   index=False)
colleges_df.to_csv('/data_processing/processed_data/collegesProcessed.csv',
                   sep=';', index=False)

# Add preference columns
colleges_df = add_preference_columns(colleges_df, len(students_df))

# Fill preferences
colleges_df = fill_preferences(colleges_df, students_df)

# Drop the 'Address' column
colleges_df = colleges_df.drop(columns=['Address'])

# Save the DataFrame to a new CSV file
colleges_df.to_csv('/data_processing/processed_data/collegesWithPreferences.csv',
                   index=False)

# Drop the 'Address' column
students_df = students_df.drop(columns=['Address'])

# Save the DataFrame to a new CSV file
students_df.to_csv('/data_processing/processed_data/studentsFinalProcessed.csv',
                   index=False)
