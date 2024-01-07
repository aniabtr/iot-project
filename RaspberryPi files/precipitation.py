import requests
import json
import time

city_name = "Hobart"
api_key = "030402bd4a5846e98aa4ba98704e797c"

#get the city's coordinates
url1= f'https://api.weatherbit.io/v2.0/current?city={city_name}&key={api_key}'
#parse the Json
req1= requests.get(url1)
data1 = req1.json()

#Let's get the longitude and latitude
lon = data1['data'][0]['lon']
lat = data1['data'][0]['lat']
#get the forcast of the next 7 days
url2 = f'https://api.weatherbit.io/v2.0/forecast/daily?lat={lat}&lon={lon}&key={api_key}'
#parse the JSON
req2 = requests.get(url2)
data2 = req2.json()
#print(f"Precipitation for {name} on {data2['data'][0]['valid_date']}: {data2['data'][0]['precip']} mm")
data2=data2['data'][0]['precip']
print(data2)