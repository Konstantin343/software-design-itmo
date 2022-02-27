curl -X POST -H "Content-Type: application/json" -d '{"name": "Name1", "login": "login1", "currency":"EUR"}' http://localhost:8080/users/register
curl -X POST -H "Content-Type: application/json" -d '{"name": "Name2", "login": "login2", "currency":"RUB"}' http://localhost:8080/users/register

curl -X POST -H "Content-Type: application/json" -d '{"name": "Product1", "description":"1", "price": 5, "owner":"login1"}' "http://localhost:8080/products/add?auth=login1"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Product2", "description":"2", "price": 150, "owner":"login2"}' "http://localhost:8080/products/add?auth=login2"
curl -X POST -H "Content-Type: application/json" -d '{"name": "Product3", "description":"3", "price": 250, "owner":"login2"}' "http://localhost:8080/products/add?auth=login2"

curl "http://localhost:8080/products?auth=login1"
curl "http://localhost:8080/products?auth=login2"