# data_processing_functions.py

from geopy.distance import geodesic
import numpy as np


# Function to calculate distance
def calculate_distance(college_coords, student_coords):
    return geodesic(college_coords, student_coords).miles


# Function to add preference columns
def add_preference_columns(df, n):
    for i in range(1, n + 1):
        df[f'Preference{i}'] = None
    return df


# Function to get sorted students
def get_sorted_students(college_coords, students_df):
    distances = students_df['Address'].apply(lambda x: calculate_distance(college_coords, x))
    sorted_students = students_df.iloc[np.argsort(distances)]['StudentName'].tolist()
    return sorted_students


# Function to fill preferences
def fill_preferences(df, students_df):
    for index, college in df.iterrows():
        college_coords = college['Address']
        sorted_students = get_sorted_students(college_coords, students_df)
        for i, student in enumerate(sorted_students, start=1):
            df.loc[index, f'Preference{i}'] = student
    return df
