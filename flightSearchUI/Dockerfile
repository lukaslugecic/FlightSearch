# Stage 1: Build React app
FROM node:18-alpine as builder

# Set working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to install dependencies
COPY package.json package-lock.json ./

# Install dependencies
RUN npm install

# Copy all source files to the container
COPY . .

# Build the React app for production
RUN npm run build

# Stage 2: Serve React app with Nginx
FROM nginx:alpine

# Copy built files from builder stage to Nginx's default HTML directory
COPY --from=builder /app/build /usr/share/nginx/html

# Expose port 80 for HTTP traffic
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
