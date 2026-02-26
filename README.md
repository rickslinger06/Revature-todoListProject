
TO run the app using docker please refere to the following instructions:

Build the Docker image: run docker build -t todo-app .

Create a .env file in the project root and add JWT_SECRET=your-secret-key

Run the container and mount the SQLite DB: run docker run -p 8080:8080 --env-file .env -v "$(pwd)/todoListDB:/data/todoListDB" --name todo-container todo-app

Access the backend in the browser at http://localhost:8080
