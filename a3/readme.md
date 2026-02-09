# CSCI 571 – Assignment 3 (Full-Stack Web Application)

Live Demo: https://csci571-a3-baisen.wl.r.appspot.com

## Overview
This project is a full-stack web application built for USC CSCI 571, demonstrating end-to-end web development: a modern Angular frontend, a Node.js/Express backend, and cloud deployment on Google App Engine. The application consumes external APIs on the server side and presents the processed data through a responsive, user-friendly UI.

## What I Built
- Developed a **single-page application** using **Angular 19.2** with client-side routing and state management
- Implemented a **RESTful backend** using **Node.js + Express** to:
  - fetch data from third-party APIs
  - normalize and filter responses
  - expose clean endpoints to the frontend
- Connected frontend and backend using **asynchronous HTTP requests** with proper loading and error handling
- Deployed the application to **Google Cloud App Engine** and validated production behavior

## Tech Stack
- **Frontend:** Angular 19.2, TypeScript, Node.js tooling
- **Backend:** Node.js, Express
- **Deployment:** Google App Engine (GCP)

## Third-Party APIs
- The frontend never calls external APIs directly
- All data fetching and transformation is handled server-side
- Backend returns a consistent JSON schema to the client

## Key Features
- Responsive UI suitable for desktop and mobile
- Client-side routing and dynamic views
- Server-side API aggregation and data transformation
- Production cloud deployment with a public URL

## Running Locally

### Backend
```bash
cd api
npm install
node server.js
```

### Frontend
```bash
cd ui
npm install
ng serve
```
