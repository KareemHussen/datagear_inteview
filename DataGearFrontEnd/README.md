# DataGear Frontend

A modern, responsive Angular application for managing financial transactions with an intuitive UI, real-time data, and comprehensive filtering capabilities.

## 📋 Overview

DataGear Frontend is an Angular-based web application that provides a beautiful, user-friendly interface for managing transactions. Built with modern Angular features like Signals, standalone components, and reactive forms.

## 🚀 Features

### Pages & Components

- **3 Main Pages**
  - Transaction List - View and filter all transactions
  - Create Transaction - Add new transactions
  - Dashboard - Real-time statistics and insights
- **Header Component** - Navigation and branding
- **Sidebar Component** - Quick navigation menu
- **Statistics Component** - Live transaction totals

### User Interface

- **Modern UI** with PrimeNG components
- **Responsive Design** that works on all devices
- **Beautiful Theming** with Lara Light Blue theme
- **Interactive Tables** with sorting and filtering
- **Success/Error Dialogs** for user feedback
- **Loading States** and skeleton screens

### Data Management

- **HTTP Client** for API communication
- **Angular Signals** for reactive state management
- **Service Layer** for business logic separation
- **Environment-based Configuration** for different deployments

### Filtering & Sorting

- **Filter by Type** (Credit/Debit)
- **Filter by Date Range** with date picker
- **Filter by Amount Range** (min/max)
- **Sort by any column** (date, amount, type)
- **Real-time Updates** as filters change
- **Debounced Search** for optimal performance

### Performance Features

- **Request Caching** to minimize API calls
- **Smart Reload** on navigation
- **Optimistic UI Updates**
- **Lazy Loading** for better initial load time

## 🛠 Technology Stack

- **Framework**: Angular 20.3.7
- **Language**: TypeScript 5.8.2
- **UI Library**: PrimeNG 20.2.0
- **Icons**: PrimeIcons 7.0.0
- **State Management**: Angular Signals
- **HTTP**: Angular HttpClient with RxJS
- **Forms**: Reactive Forms
- **Build Tool**: Angular CLI

## 📁 Project Structure

```
src/
├── app/
│   ├── components/
│   │   ├── header/              # Header component
│   │   ├── sidebar/              # Sidebar navigation
│   │   ├── statistics/           # Statistics display
│   │   ├── transaction-form/    # Create transaction form
│   │   └── transactions/        # Transaction list & filters
│   ├── models/                   # TypeScript interfaces
│   ├── services/
│   │   └── transaction.service.ts # API service layer
│   ├── app.config.ts            # App configuration
│   ├── app.routes.ts            # Routing configuration
│   └── app.ts                   # Root component
├── assets/                       # Static assets
├── environments/                 # Environment configuration
│   ├── environment.ts           # Development config
│   └── environment.prod.ts      # Production config
└── index.html                    # Entry HTML
```

## 🏃 Running the Application

### Prerequisites

- Node.js 18+
- npm or yarn

### Installation & Development

```bash
# Navigate to frontend directory
cd DataGearFrontEnd

# Install dependencies
npm install

# Start development server
npm start

# The app will be available at http://localhost:4200
```

### Build for Production

```bash
# Build the application
npm run build

# The output will be in dist/DataGearFrontEnd/
```

### Using Docker

```bash
# Build the image
docker build -t datagear-frontend .

# Run the container
docker run -p 4200:80 datagear-frontend
```

### Using Docker Compose (Recommended)

From the root directory:

```bash
docker-compose up frontend
```

## 🌐 Access

- **Development**: `http://localhost:4200`
- **Production**: `http://localhost:4200` (via Docker)

## 🎨 UI Features

### Components Used from PrimeNG

- **p-table** - Data tables with pagination
- **p-calendar** - Date range selection
- **p-dropdown** - Filter selection
- **p-dialog** - Success/error dialogs
- **p-button** - Action buttons
- **p-card** - Content cards
- **p-message** - User notifications
- **p-skeleton** - Loading states

## 📡 API Integration

The frontend communicates with the backend API using environment-based configuration:

- **Development**: `http://127.0.0.1:8080/api/v1`
- **Production**: Dynamic API URL based on deployment

### Service Layer

- `TransactionService` handles all API calls
- Request caching for performance
- Error handling with user-friendly messages
- Type-safe with TypeScript interfaces

## 🔧 Configuration

### Environment Files

**src/environments/environment.ts** (Development)

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://127.0.0.1:8080/api/v1'
};
```

**src/environments/environment.prod.ts** (Production)

```typescript
export const environment = {
  production: true,
  apiUrl: window.location.protocol + '//' + window.location.hostname + ':8080/api/v1'
};
```

## 📱 Responsive Design

The application is fully responsive and works on:

- Desktop (1920px+)
- Laptop (1024px+)
- Tablet (768px+)
- Mobile (320px+)

## 🎯 Key Features in Detail

### Transaction Management

- Create new transactions with validation
- View all transactions in a paginated table
- Real-time filtering and sorting
- Auto-refresh after creating new transactions

### Statistics Dashboard

- Total credits and debits
- Transaction counts
- Real-time updates

### User Experience

- Loading indicators
- Error handling with clear messages
- Success confirmations
- Intuitive navigation

## 📦 Dependencies

### Main Dependencies

- `@angular/core` - Angular framework
- `@angular/router` - Routing
- `@angular/forms` - Form handling
- `@angular/common/http` - HTTP client
- `primeng` - UI component library
- `rxjs` - Reactive programming

### Dev Dependencies

- `@angular/cli` - Development tools
- `typescript` - TypeScript compiler

## 🐛 Troubleshooting

### Port Already in Use

```bash
# Use a different port
ng serve --port 4201
```

### Installation Issues

```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Build Issues

```bash
# Clear Angular cache
rm -rf .angular dist
ng build
```
