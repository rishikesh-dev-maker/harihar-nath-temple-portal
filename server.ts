import express, { Request, Response, NextFunction } from "express";
import path from "path";
import jwt from "jsonwebtoken";
import bcrypt from "bcryptjs";
import { createServer as createViteServer } from "vite";

const app = express();
const PORT = 3000;

app.use(express.json({ limit: "25mb" }));
app.use(express.urlencoded({ extended: true, limit: "25mb" }));

// Static file serving for public directory and assets
app.use(express.static(path.join(process.cwd(), "public")));
app.use("/assets", express.static(path.join(process.cwd(), "public/assets")));

// JWT Secret Key
const JWT_SECRET = process.env.JWT_SECRET || "dGhpc2lzYXZlcnlzZWN1cmVhbmRzdHJvbmdzZWNyZXRrZXlmb3JoYXJpaGFybmF0aHRlbXBsZXByb2plY3QyMDI2c2VjdXJl";

// ==============================================================================
// IN-MEMORY DATA STORE (Seeded with initial data matching Spring Boot DataInitializer)
// ==============================================================================

interface User {
  id: number;
  name: string;
  email: string;
  passwordHash: string;
  role: "ADMIN" | "STAFF";
  enabled: boolean;
  createdAt: string;
}

interface Booking {
  id: number;
  requestId: string;
  service: string;
  bookingDate: string;
  name: string;
  mobile: string;
  email: string;
  devotees: number;
  notes: string;
  status: "PENDING" | "CONFIRMED" | "CANCELLED" | "COMPLETED";
  createdAt: string;
  updatedAt: string;
}

interface ContactMessage {
  id: number;
  name: string;
  email: string;
  subject: string;
  message: string;
  status: "NEW" | "READ" | "REPLIED" | "ARCHIVED";
  createdAt: string;
}

interface Announcement {
  id: number;
  title: string;
  message: string;
  active: boolean;
  displayOrder: number;
  startDate?: string;
  endDate?: string;
  createdAt: string;
  updatedAt: string;
}

interface TempleService {
  id: number;
  name: string;
  description: string;
  price: number;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

interface AartiSchedule {
  id: number;
  name: string;
  time: string;
  description: string;
  active: boolean;
  displayOrder: number;
}

interface GalleryItem {
  id: number;
  title: string;
  imageUrl: string;
  description: string;
  category: string;
  active: boolean;
  displayOrder: number;
  createdAt: string;
}

// Initial Admin User (admin@hariharnath.in / Admin@123)
const salt = bcrypt.genSaltSync(10);
const adminPasswordHash = bcrypt.hashSync("Admin@123", salt);

const users: User[] = [
  {
    id: 1,
    name: "Temple Administrator",
    email: "admin@hariharnath.in",
    passwordHash: adminPasswordHash,
    role: "ADMIN",
    enabled: true,
    createdAt: new Date().toISOString()
  },
  {
    id: 2,
    name: "Rishikesh Kumar",
    email: "rishikkr725@gmail.com",
    passwordHash: adminPasswordHash,
    role: "ADMIN",
    enabled: true,
    createdAt: new Date().toISOString()
  }
];

let bookingCounter = 1;
const bookings: Booking[] = [
  {
    id: 1,
    requestId: "BHT-2026-000001",
    service: "Rudrabhishek",
    bookingDate: "2026-09-20",
    name: "Rishikesh Kumar",
    mobile: "9876543210",
    email: "rishikkr725@gmail.com",
    devotees: 2,
    notes: "Special family sankalp puja at morning aarti",
    status: "CONFIRMED",
    createdAt: new Date(Date.now() - 86400000).toISOString(),
    updatedAt: new Date(Date.now() - 43200000).toISOString()
  }
];

const contactMessages: ContactMessage[] = [
  {
    id: 1,
    name: "Devotee Visitor",
    email: "visitor@gmail.com",
    subject: "Temple enquiry regarding Kartik Purnima",
    message: "Kindly inform if special guest passes are required for Kartik Purnima evening aarti.",
    status: "NEW",
    createdAt: new Date().toISOString()
  }
];

const announcements: Announcement[] = [
  {
    id: 1,
    title: "Kartik Purnima Mahotsav",
    message: "🪔 Daily Darshan & Aarti Schedule · Kartik Purnima preparations begin at Harihar Kshetra.",
    active: true,
    displayOrder: 1,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 2,
    title: "Sacred Sandhya Aarti",
    message: "🙏 Join us for the sacred Sandhya Aarti every evening at Harihar Kshetra.",
    active: true,
    displayOrder: 2,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 3,
    title: "Advance Darshan Advisory",
    message: "Temple timings may vary on festival days · Advance darshan booking recommended.",
    active: true,
    displayOrder: 3,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  }
];

const templeServices: TempleService[] = [
  {
    id: 1,
    name: "Nitya Abhishek",
    description: "Daily ritual bathing of the deity with sacred water, milk, and offerings.",
    price: 251,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 2,
    name: "Archana",
    description: "Offering of sacred flowers and chanting of divine names for auspicious blessings.",
    price: 101,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 3,
    name: "Rudrabhishek",
    description: "Special Vedic chanting worship with Rudram hymns for peace, health, and prosperity.",
    price: 1100,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  },
  {
    id: 4,
    name: "Special Puja",
    description: "Personalized family sankalp puja conducted by temple priests.",
    price: 501,
    active: true,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  }
];

const aartiSchedules: AartiSchedule[] = [
  { id: 1, name: "Mangala Aarti", time: "04:00 / 05:00", description: "Morning awakening", active: true, displayOrder: 1 },
  { id: 2, name: "Nitya Abhishek", time: "06:00–07:00", description: "Sacred bathing", active: true, displayOrder: 2 },
  { id: 3, name: "Rajbhog", time: "11:30–12:00", description: "Royal offering", active: true, displayOrder: 3 },
  { id: 4, name: "Shayan", time: "12:00–14:00", description: "Midday rest", active: true, displayOrder: 4 },
  { id: 5, name: "Devotee Puja", time: "14:00–16:00", description: "Public worship", active: true, displayOrder: 5 },
  { id: 6, name: "Sandhya Aarti", time: "18:30 / 19:30", description: "Evening illumination", active: true, displayOrder: 6 },
  { id: 7, name: "Bhog", time: "20:30", description: "Evening offering", active: true, displayOrder: 7 },
  { id: 8, name: "Shayan", time: "21:00", description: "Night rest", active: true, displayOrder: 8 }
];

const galleryItems: GalleryItem[] = [
  {
    id: 1,
    title: "Deity Darshan",
    imageUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEBjfuvG44ar02MZKAlMvbOd213M4CacO5a6ug86Ak6BGNuetf3w0NJ7PF&s=10",
    description: "Sacred Sanctum of Hariharnath",
    category: "DEITY",
    active: true,
    displayOrder: 1,
    createdAt: new Date().toISOString()
  },
  {
    id: 2,
    title: "River Worship",
    imageUrl: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTm1Hv20p2zdnIe2Nhu7ucJHej97_CedzwtQy9T_ksd0w&s=10",
    description: "Ganga-Gandak confluence snan",
    category: "RITUAL",
    active: true,
    displayOrder: 2,
    createdAt: new Date().toISOString()
  },
  {
    id: 3,
    title: "Sacred Architecture",
    imageUrl: "https://coinventmediastorage.blob.core.windows.net/media-storage-container/gphoto_Ei1IYXJpaGFyYW5hdGggUmQsIFNvbmVwdXIsIEJpaGFyIDg0MTEwMSwgSW5kaWEiLiosChQKEgkf0YkW-FvtOREAMVfIJDjarxIUChIJm2NsaOpb7TkRjbMEPEFXXxc_0.jpg",
    description: "Ancient stone spire",
    category: "ARCHITECTURE",
    active: true,
    displayOrder: 3,
    createdAt: new Date().toISOString()
  },
  {
    id: 4,
    title: "Evening Lamps",
    imageUrl: "image.png",
    description: "Temple courtyard lighted with deepaks and festive illumination",
    category: "AARTI",
    active: true,
    displayOrder: 4,
    createdAt: new Date().toISOString()
  }
];

// ==============================================================================
// AUTHENTICATION MIDDLEWARE
// ==============================================================================
function authenticateToken(req: Request, res: Response, next: NextFunction) {
  const authHeader = req.headers["authorization"];
  const token = authHeader && authHeader.split(" ")[1];

  if (!token) {
    res.status(401).json({
      success: false,
      message: "Unauthorized: Access token is missing",
      timestamp: new Date().toISOString(),
      path: req.originalUrl
    });
    return;
  }

  jwt.verify(token, JWT_SECRET, (err: any, decoded: any) => {
    if (err) {
      res.status(401).json({
        success: false,
        message: "Unauthorized: Invalid or expired token",
        timestamp: new Date().toISOString(),
        path: req.originalUrl
      });
      return;
    }
    (req as any).user = decoded;
    next();
  });
}

// ==============================================================================
// REST APIS (/api/v1)
// ==============================================================================

// Health Check
app.get("/api/v1/health", (req, res) => {
  res.json({
    status: "UP",
    service: "Baba Hariharnath Temple Backend API",
    location: "Sonepur, Saran, Bihar",
    timestamp: new Date().toISOString()
  });
});

// --- AUTHENTICATION ---
app.post("/api/v1/auth/login", (req, res) => {
  const { email, password } = req.body;

  if (!email || !password) {
    res.status(400).json({
      success: false,
      message: "Validation failed",
      errors: {
        email: !email ? "Email is required" : undefined,
        password: !password ? "Password is required" : undefined
      }
    });
    return;
  }

  const user = users.find(u => u.email.toLowerCase() === email.toLowerCase());
  if (!user || !bcrypt.compareSync(password, user.passwordHash)) {
    res.status(401).json({
      success: false,
      message: "Invalid email or password credentials",
      timestamp: new Date().toISOString(),
      path: req.originalUrl
    });
    return;
  }

  if (!user.enabled) {
    res.status(403).json({
      success: false,
      message: "Account is disabled. Please contact administrator.",
      timestamp: new Date().toISOString(),
      path: req.originalUrl
    });
    return;
  }

  const token = jwt.sign(
    { id: user.id, name: user.name, email: user.email, role: user.role },
    JWT_SECRET,
    { expiresIn: "24h" }
  );

  const refreshToken = jwt.sign(
    { email: user.email, type: "REFRESH" },
    JWT_SECRET,
    { expiresIn: "7d" }
  );

  res.json({
    token,
    type: "Bearer",
    refreshToken,
    user: {
      id: user.id,
      name: user.name,
      email: user.email,
      role: user.role,
      enabled: user.enabled,
      createdAt: user.createdAt
    }
  });
});

app.post("/api/v1/auth/register", (req, res) => {
  const { name, email, password, role } = req.body;

  if (!name || !email || !password) {
    res.status(400).json({
      success: false,
      message: "Validation failed",
      errors: {
        name: !name ? "Name is required" : undefined,
        email: !email ? "Email is required" : undefined,
        password: !password ? "Password is required" : undefined
      }
    });
    return;
  }

  if (users.some(u => u.email.toLowerCase() === email.toLowerCase())) {
    res.status(409).json({
      success: false,
      message: "Email is already registered: " + email,
      timestamp: new Date().toISOString(),
      path: req.originalUrl
    });
    return;
  }

  const newUser: User = {
    id: users.length + 1,
    name,
    email,
    passwordHash: bcrypt.hashSync(password, 10),
    role: role === "ADMIN" ? "ADMIN" : "STAFF",
    enabled: true,
    createdAt: new Date().toISOString()
  };

  users.push(newUser);

  const token = jwt.sign(
    { id: newUser.id, name: newUser.name, email: newUser.email, role: newUser.role },
    JWT_SECRET,
    { expiresIn: "24h" }
  );

  res.status(201).json({
    token,
    type: "Bearer",
    user: {
      id: newUser.id,
      name: newUser.name,
      email: newUser.email,
      role: newUser.role,
      enabled: newUser.enabled,
      createdAt: newUser.createdAt
    }
  });
});

app.post("/api/v1/auth/refresh", (req, res) => {
  const { refreshToken } = req.body;
  if (!refreshToken) {
    res.status(400).json({ success: false, message: "Refresh token is required" });
    return;
  }

  try {
    const decoded = jwt.verify(refreshToken, JWT_SECRET) as any;
    const user = users.find(u => u.email === decoded.email);
    if (!user) {
      res.status(401).json({ success: false, message: "User not found" });
      return;
    }

    const token = jwt.sign(
      { id: user.id, name: user.name, email: user.email, role: user.role },
      JWT_SECRET,
      { expiresIn: "24h" }
    );

    res.json({
      token,
      type: "Bearer",
      user: {
        id: user.id,
        name: user.name,
        email: user.email,
        role: user.role
      }
    });
  } catch (err) {
    res.status(401).json({ success: false, message: "Invalid or expired refresh token" });
  }
});

// --- PUBLIC BOOKINGS ---
app.post("/api/v1/bookings", (req, res) => {
  const { service, bookingDate, name, mobile, email, devotees, notes } = req.body;

  const errors: Record<string, string> = {};

  if (!service || !service.trim()) errors.service = "Service is required";
  if (!bookingDate) errors.bookingDate = "Booking date is required";
  if (!name || name.trim().length < 2) errors.name = "Name is required and must be at least 2 characters";

  // Validate Indian mobile
  const phoneClean = mobile ? String(mobile).replace(/\D/g, "") : "";
  if (!phoneClean || phoneClean.length !== 10 || !/^[6-9]\d{9}$/.test(phoneClean)) {
    errors.mobile = "Please provide a valid 10-digit Indian mobile number (starts with 6,7,8,9)";
  }

  // Validate email
  if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    errors.email = "Please provide a valid email address";
  }

  const devoteeCount = Number(devotees);
  if (!devoteeCount || devoteeCount < 1 || devoteeCount > 10) {
    errors.devotees = "Devotees count must be between 1 and 10";
  }

  if (Object.keys(errors).length > 0) {
    res.status(400).json({
      success: false,
      message: "Validation failed",
      errors
    });
    return;
  }

  bookingCounter++;
  const currentYear = new Date().getFullYear();
  const requestId = `BHT-${currentYear}-${String(bookingCounter).padStart(6, "0")}`;

  const newBooking: Booking = {
    id: bookingCounter,
    requestId,
    service: service.trim(),
    bookingDate,
    name: name.trim(),
    mobile: phoneClean,
    email: email.trim(),
    devotees: devoteeCount,
    notes: notes ? notes.trim() : "",
    status: "PENDING",
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };

  bookings.unshift(newBooking);

  // Log simulated email confirmation
  console.log(`[EMAIL SERVICE] Booking Confirmation sent to: ${newBooking.email} for Request ID: ${newBooking.requestId}`);
  console.log(`[EMAIL SERVICE] Admin alert sent to: info@hariharnath.in, booking@hariharnath.in`);

  res.status(201).json({
    success: true,
    message: "Booking request received successfully",
    requestId: newBooking.requestId,
    status: newBooking.status,
    service: newBooking.service,
    bookingDate: newBooking.bookingDate,
    name: newBooking.name,
    mobile: newBooking.mobile,
    email: newBooking.email,
    devotees: newBooking.devotees,
    notes: newBooking.notes,
    createdAt: newBooking.createdAt
  });
});

app.get("/api/v1/bookings/:requestId", (req, res) => {
  const { requestId } = req.params;
  const booking = bookings.find(b => b.requestId.toLowerCase() === requestId.toLowerCase());

  if (!booking) {
    res.status(404).json({
      success: false,
      message: "Booking not found with Request ID: " + requestId,
      timestamp: new Date().toISOString(),
      path: req.originalUrl
    });
    return;
  }

  res.json({
    success: true,
    message: "Booking details retrieved successfully",
    requestId: booking.requestId,
    status: booking.status,
    service: booking.service,
    bookingDate: booking.bookingDate,
    name: booking.name,
    mobile: booking.mobile,
    email: booking.email,
    devotees: booking.devotees,
    notes: booking.notes,
    createdAt: booking.createdAt
  });
});

// --- PUBLIC CONTACT ---
app.post("/api/v1/contact", (req, res) => {
  const { name, email, subject, message } = req.body;

  const errors: Record<string, string> = {};
  if (!name || !name.trim()) errors.name = "Name is required";
  if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) errors.email = "Please provide a valid email address";
  if (!subject || !subject.trim()) errors.subject = "Subject is required";
  if (!message || !message.trim()) errors.message = "Message is required";

  if (Object.keys(errors).length > 0) {
    res.status(400).json({
      success: false,
      message: "Validation failed",
      errors
    });
    return;
  }

  const newContact: ContactMessage = {
    id: contactMessages.length + 1,
    name: name.trim(),
    email: email.trim(),
    subject: subject.trim(),
    message: message.trim(),
    status: "NEW",
    createdAt: new Date().toISOString()
  };

  contactMessages.unshift(newContact);
  console.log(`[CONTACT SERVICE] New message received from: ${newContact.email} - Subject: ${newContact.subject}`);

  res.status(201).json({
    success: true,
    message: "Message sent successfully.",
    data: newContact
  });
});

// --- PUBLIC ANNOUNCEMENTS ---
app.get("/api/v1/announcements", (req, res) => {
  const activeList = announcements
    .filter(a => a.active)
    .sort((a, b) => a.displayOrder - b.displayOrder);
  res.json(activeList);
});

// --- PUBLIC SERVICES ---
app.get("/api/v1/services", (req, res) => {
  const activeServices = templeServices.filter(s => s.active);
  res.json(activeServices);
});

// --- PUBLIC AARTI ---
app.get("/api/v1/aarti", (req, res) => {
  const activeAartis = aartiSchedules
    .filter(a => a.active)
    .sort((a, b) => a.displayOrder - b.displayOrder);
  res.json(activeAartis);
});

// --- PUBLIC GALLERY ---
app.get("/api/v1/gallery", (req, res) => {
  const activeItems = galleryItems
    .filter(g => g.active)
    .sort((a, b) => a.displayOrder - b.displayOrder);
  res.json(activeItems);
});

// ==============================================================================
// ADMIN PROTECTED APIS (/api/v1/admin/*)
// ==============================================================================

// Dashboard Stats
app.get("/api/v1/admin/dashboard/stats", authenticateToken, (req, res) => {
  const todayStr = new Date().toISOString().split("T")[0];

  const totalBookings = bookings.length;
  const pendingBookings = bookings.filter(b => b.status === "PENDING").length;
  const confirmedBookings = bookings.filter(b => b.status === "CONFIRMED").length;
  const cancelledBookings = bookings.filter(b => b.status === "CANCELLED").length;
  const completedBookings = bookings.filter(b => b.status === "COMPLETED").length;
  const todayBookings = bookings.filter(b => b.bookingDate === todayStr).length;

  const totalContactMessages = contactMessages.length;
  const unreadContactMessages = contactMessages.filter(m => m.status === "NEW").length;
  const activeServices = templeServices.filter(s => s.active).length;
  const activeAnnouncements = announcements.filter(a => a.active).length;

  res.json({
    totalBookings,
    pendingBookings,
    confirmedBookings,
    cancelledBookings,
    completedBookings,
    todayBookings,
    totalContactMessages,
    unreadContactMessages,
    activeServices,
    activeAnnouncements
  });
});

// Admin Bookings List & Filter
app.get("/api/v1/admin/bookings", authenticateToken, (req, res) => {
  const { status, date, search, page = "0", size = "20" } = req.query;

  let filtered = [...bookings];

  if (status) {
    filtered = filtered.filter(b => b.status === String(status).toUpperCase());
  }

  if (date) {
    filtered = filtered.filter(b => b.bookingDate === String(date));
  }

  if (search) {
    const q = String(search).toLowerCase();
    filtered = filtered.filter(b =>
      b.name.toLowerCase().includes(q) ||
      b.requestId.toLowerCase().includes(q) ||
      b.email.toLowerCase().includes(q) ||
      b.mobile.includes(q)
    );
  }

  const pageNum = parseInt(String(page), 10) || 0;
  const pageSize = parseInt(String(size), 10) || 20;
  const startIndex = pageNum * pageSize;
  const pagedList = filtered.slice(startIndex, startIndex + pageSize);

  res.json({
    content: pagedList,
    totalElements: filtered.length,
    totalPages: Math.ceil(filtered.length / pageSize),
    number: pageNum,
    size: pageSize
  });
});

// Update Booking Status
app.put("/api/v1/admin/bookings/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const { status } = req.body;

  const validStatuses = ["PENDING", "CONFIRMED", "CANCELLED", "COMPLETED"];
  if (!status || !validStatuses.includes(status)) {
    res.status(400).json({
      success: false,
      message: "Valid status required: PENDING, CONFIRMED, CANCELLED, COMPLETED"
    });
    return;
  }

  const booking = bookings.find(b => b.id === id);
  if (!booking) {
    res.status(404).json({ success: false, message: "Booking not found with id: " + id });
    return;
  }

  booking.status = status as any;
  booking.updatedAt = new Date().toISOString();

  console.log(`[EMAIL SERVICE] Status update (${status}) sent to devotee: ${booking.email} for ${booking.requestId}`);

  res.json(booking);
});

// Delete Booking
app.delete("/api/v1/admin/bookings/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const index = bookings.findIndex(b => b.id === id);
  if (index === -1) {
    res.status(404).json({ success: false, message: "Booking not found with id: " + id });
    return;
  }
  bookings.splice(index, 1);
  res.json({ success: true, message: "Booking deleted successfully" });
});

// Admin Contact Messages
app.get("/api/v1/admin/contact", authenticateToken, (req, res) => {
  const { status } = req.query;
  let list = [...contactMessages];
  if (status) {
    list = list.filter(m => m.status === String(status).toUpperCase());
  }
  res.json({
    content: list,
    totalElements: list.length
  });
});

app.put("/api/v1/admin/contact/:id/status", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const { status } = req.query;

  const message = contactMessages.find(m => m.id === id);
  if (!message) {
    res.status(404).json({ success: false, message: "Message not found with id: " + id });
    return;
  }

  message.status = String(status).toUpperCase() as any;
  res.json(message);
});

app.delete("/api/v1/admin/contact/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const index = contactMessages.findIndex(m => m.id === id);
  if (index === -1) {
    res.status(404).json({ success: false, message: "Message not found with id: " + id });
    return;
  }
  contactMessages.splice(index, 1);
  res.json({ success: true, message: "Contact message deleted successfully" });
});

// Admin Announcements
app.get("/api/v1/admin/announcements", authenticateToken, (req, res) => {
  res.json(announcements);
});

app.post("/api/v1/admin/announcements", authenticateToken, (req, res) => {
  const { title, message, active = true, displayOrder = 0, startDate, endDate } = req.body;
  if (!title || !message) {
    res.status(400).json({ success: false, message: "Title and message are required" });
    return;
  }
  const item: Announcement = {
    id: announcements.length + 1,
    title,
    message,
    active,
    displayOrder,
    startDate,
    endDate,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };
  announcements.push(item);
  res.status(201).json(item);
});

app.put("/api/v1/admin/announcements/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const item = announcements.find(a => a.id === id);
  if (!item) {
    res.status(404).json({ success: false, message: "Announcement not found with id: " + id });
    return;
  }
  Object.assign(item, req.body, { updatedAt: new Date().toISOString() });
  res.json(item);
});

app.delete("/api/v1/admin/announcements/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const idx = announcements.findIndex(a => a.id === id);
  if (idx === -1) {
    res.status(404).json({ success: false, message: "Announcement not found" });
    return;
  }
  announcements.splice(idx, 1);
  res.json({ success: true, message: "Announcement deleted successfully" });
});

// Admin Services
app.post("/api/v1/admin/services", authenticateToken, (req, res) => {
  const { name, description, price, active = true } = req.body;
  const item: TempleService = {
    id: templeServices.length + 1,
    name,
    description,
    price: Number(price) || 0,
    active,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };
  templeServices.push(item);
  res.status(201).json(item);
});

app.put("/api/v1/admin/services/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const item = templeServices.find(s => s.id === id);
  if (!item) {
    res.status(404).json({ success: false, message: "Service not found" });
    return;
  }
  Object.assign(item, req.body, { updatedAt: new Date().toISOString() });
  res.json(item);
});

app.delete("/api/v1/admin/services/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const idx = templeServices.findIndex(s => s.id === id);
  if (idx === -1) {
    res.status(404).json({ success: false, message: "Service not found" });
    return;
  }
  templeServices.splice(idx, 1);
  res.json({ success: true, message: "Service deleted successfully" });
});

// Admin Aarti
app.post("/api/v1/admin/aarti", authenticateToken, (req, res) => {
  const { name, time, description, active = true, displayOrder = 0 } = req.body;
  const item: AartiSchedule = {
    id: aartiSchedules.length + 1,
    name,
    time,
    description,
    active,
    displayOrder
  };
  aartiSchedules.push(item);
  res.status(201).json(item);
});

app.put("/api/v1/admin/aarti/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const item = aartiSchedules.find(a => a.id === id);
  if (!item) {
    res.status(404).json({ success: false, message: "Aarti schedule not found" });
    return;
  }
  Object.assign(item, req.body);
  res.json(item);
});

app.delete("/api/v1/admin/aarti/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const idx = aartiSchedules.findIndex(a => a.id === id);
  if (idx === -1) {
    res.status(404).json({ success: false, message: "Aarti schedule not found" });
    return;
  }
  aartiSchedules.splice(idx, 1);
  res.json({ success: true, message: "Aarti schedule deleted successfully" });
});

// Admin Gallery
app.post("/api/v1/admin/gallery", authenticateToken, (req, res) => {
  const { title, imageUrl, description, category = "GENERAL", active = true, displayOrder = 0 } = req.body;
  const item: GalleryItem = {
    id: galleryItems.length + 1,
    title,
    imageUrl,
    description,
    category,
    active,
    displayOrder,
    createdAt: new Date().toISOString()
  };
  galleryItems.push(item);
  res.status(201).json(item);
});

app.put("/api/v1/admin/gallery/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const item = galleryItems.find(g => g.id === id);
  if (!item) {
    res.status(404).json({ success: false, message: "Gallery item not found" });
    return;
  }
  Object.assign(item, req.body);
  res.json(item);
});

app.delete("/api/v1/admin/gallery/:id", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const idx = galleryItems.findIndex(g => g.id === id);
  if (idx === -1) {
    res.status(404).json({ success: false, message: "Gallery item not found" });
    return;
  }
  galleryItems.splice(idx, 1);
  res.json({ success: true, message: "Gallery item deleted successfully" });
});

// Admin Users
app.get("/api/v1/admin/users", authenticateToken, (req, res) => {
  const safeUsers = users.map(u => ({
    id: u.id,
    name: u.name,
    email: u.email,
    role: u.role,
    enabled: u.enabled,
    createdAt: u.createdAt
  }));
  res.json(safeUsers);
});

app.put("/api/v1/admin/users/:id/status", authenticateToken, (req, res) => {
  const id = parseInt(req.params.id, 10);
  const { enabled } = req.query;
  const user = users.find(u => u.id === id);
  if (!user) {
    res.status(404).json({ success: false, message: "User not found" });
    return;
  }
  user.enabled = enabled === "true";
  res.json({ success: true, message: "User status updated" });
});

// Admin Password Update (Only for authenticated user)
app.put("/api/v1/admin/change-password", authenticateToken, (req, res) => {
  const { currentPassword, newPassword } = req.body;
  const userId = (req as any).user.id;
  const user = users.find(u => u.id === userId);

  if (!user) {
    res.status(404).json({ success: false, message: "User not found" });
    return;
  }

  if (!currentPassword || !newPassword) {
    res.status(400).json({ success: false, message: "Both current password and new password are required" });
    return;
  }

  if (!bcrypt.compareSync(currentPassword, user.passwordHash)) {
    res.status(400).json({ success: false, message: "Current password is incorrect" });
    return;
  }

  if (newPassword.length < 6) {
    res.status(400).json({ success: false, message: "New password must be at least 6 characters long" });
    return;
  }

  user.passwordHash = bcrypt.hashSync(newPassword, 10);
  console.log(`[AUTH] Password updated successfully for user: ${user.email}`);

  res.json({ success: true, message: "Password updated successfully. Please keep it safe." });
});

// ==============================================================================
// VITE MIDDLEWARE & STATIC SERVING
// ==============================================================================
async function startServer() {
  if (process.env.NODE_ENV !== "production") {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: "spa",
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), "dist");
    app.use(express.static(distPath));
    app.get("*", (req, res) => {
      res.sendFile(path.join(distPath, "index.html"));
    });
  }

  app.listen(PORT, "0.0.0.0", () => {
    console.log(`🛕 Baba Hariharnath Temple Portal Server running on http://0.0.0.0:${PORT}`);
  });
}

startServer();
