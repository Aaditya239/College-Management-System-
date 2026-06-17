/*
 * College Management System MongoDB Setup Script
 * Database: college_management_db
 *
 * Run in MongoDB Shell / mongosh:
 *   mongosh < mongo/college_management_db_setup.js
 *
 * This script creates:
 * - collections
 * - JSON Schema validation rules
 * - indexes
 * - sample data
 * - commented CRUD, aggregation, and lookup examples
 */

const appDb = db.getSiblingDB("college_management_db");

print("\nResetting database: college_management_db");
appDb.dropDatabase();

function pad(number, size = 4) {
  return String(number).padStart(size, "0");
}

function slugify(value) {
  return value
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, ".")
    .replace(/^\.|\.$/g, "");
}

function createValidatedCollection(collectionName, schema, indexes) {
  appDb.createCollection(collectionName, {
    validator: {
      $jsonSchema: schema,
    },
    validationLevel: "strict",
    validationAction: "error",
  });

  (indexes || []).forEach(function (indexSpec) {
    appDb[collectionName].createIndex(indexSpec.keys, indexSpec.options || {});
  });
}

function printSection(title) {
  print("\n==================================================");
  print(title);
  print("==================================================");
}

printSection("1) Creating collections, validations, and indexes");

// -------------------------------------------------------------------
// Collection Schemas
// -------------------------------------------------------------------
// Notes:
// - Custom string IDs are used as _id values for all collections.
// - Relationships are stored as references using those custom IDs.
// - MongoDB validates structure using JSON Schema.

createValidatedCollection(
  "departments",
  {
    bsonType: "object",
    required: ["_id", "departmentName", "hodId"],
    additionalProperties: false,
    properties: {
      _id: {
        bsonType: "string",
        description: "Custom department ID, for example DEP-1001",
      },
      departmentName: {
        bsonType: "string",
        minLength: 2,
        maxLength: 120,
        description: "Department name",
      },
      hodId: {
        bsonType: "string",
        description: "Reference to staff._id for the HOD",
      },
    },
  },
  [
    {
      keys: { departmentName: 1 },
      options: { name: "uq_department_name", unique: true },
    },
    {
      keys: { hodId: 1 },
      options: { name: "uq_department_hod", unique: true, sparse: true },
    },
  ]
);

createValidatedCollection(
  "staff",
  {
    bsonType: "object",
    required: ["_id", "name", "email", "designation", "salary", "departmentId"],
    additionalProperties: false,
    properties: {
      _id: {
        bsonType: "string",
        description: "Custom staff ID, for example STF-1001",
      },
      name: {
        bsonType: "string",
        minLength: 2,
        maxLength: 100,
      },
      email: {
        bsonType: "string",
        pattern: "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$",
      },
      designation: {
        bsonType: "string",
        minLength: 2,
        maxLength: 100,
      },
      salary: {
        bsonType: ["int", "long", "double", "decimal"],
        minimum: 0,
      },
      departmentId: {
        bsonType: "string",
        description: "Reference to departments._id",
      },
      hodDepartmentId: {
        bsonType: "string",
        description: "Optional reverse HOD reference; unique when present",
      },
    },
  },
  [
    {
      keys: { email: 1 },
      options: { name: "uq_staff_email", unique: true },
    },
    {
      keys: { departmentId: 1 },
      options: { name: "idx_staff_department" },
    },
    {
      keys: { hodDepartmentId: 1 },
      options: { name: "uq_staff_hod_department", unique: true, sparse: true },
    },
  ]
);

createValidatedCollection(
  "courses",
  {
    bsonType: "object",
    required: ["_id", "courseName", "credits", "departmentId", "studentIds"],
    additionalProperties: false,
    properties: {
      _id: {
        bsonType: "string",
        description: "Custom course ID, for example CRS-1001",
      },
      courseName: {
        bsonType: "string",
        minLength: 2,
        maxLength: 120,
      },
      credits: {
        bsonType: ["int", "long"],
        minimum: 1,
      },
      departmentId: {
        bsonType: "string",
        description: "Reference to departments._id",
      },
      studentIds: {
        bsonType: "array",
        description: "Many-to-many reference list to students._id",
        items: {
          bsonType: "string",
        },
      },
    },
  },
  [
    {
      keys: { courseName: 1 },
      options: { name: "uq_course_name", unique: true },
    },
    {
      keys: { departmentId: 1 },
      options: { name: "idx_course_department" },
    },
    {
      keys: { studentIds: 1 },
      options: { name: "idx_course_students" },
    },
  ]
);

createValidatedCollection(
  "students",
  {
    bsonType: "object",
    required: ["_id", "name", "email", "phone", "address", "departmentId", "courseIds"],
    additionalProperties: false,
    properties: {
      _id: {
        bsonType: "string",
        description: "Custom student ID, for example STU-1001",
      },
      name: {
        bsonType: "string",
        minLength: 2,
        maxLength: 100,
      },
      email: {
        bsonType: "string",
        pattern: "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$",
      },
      phone: {
        bsonType: "string",
        minLength: 10,
        maxLength: 15,
      },
      address: {
        bsonType: "string",
        minLength: 2,
        maxLength: 200,
      },
      departmentId: {
        bsonType: "string",
        description: "Reference to departments._id",
      },
      courseIds: {
        bsonType: "array",
        description: "Many-to-many reference list to courses._id",
        items: {
          bsonType: "string",
        },
      },
    },
  },
  [
    {
      keys: { email: 1 },
      options: { name: "uq_student_email", unique: true },
    },
    {
      keys: { departmentId: 1 },
      options: { name: "idx_student_department" },
    },
    {
      keys: { courseIds: 1 },
      options: { name: "idx_student_courses" },
    },
  ]
);

createValidatedCollection(
  "books",
  {
    bsonType: "object",
    required: ["_id", "title", "author", "isbn", "quantity"],
    additionalProperties: false,
    properties: {
      _id: {
        bsonType: "string",
        description: "Custom book ID, for example BOK-1001",
      },
      title: {
        bsonType: "string",
        minLength: 2,
        maxLength: 180,
      },
      author: {
        bsonType: "string",
        minLength: 2,
        maxLength: 120,
      },
      isbn: {
        bsonType: "string",
        pattern: "^[0-9]{13}$",
      },
      quantity: {
        bsonType: ["int", "long"],
        minimum: 0,
      },
    },
  },
  [
    {
      keys: { isbn: 1 },
      options: { name: "uq_book_isbn", unique: true },
    },
    {
      keys: { title: 1 },
      options: { name: "idx_book_title" },
    },
  ]
);

createValidatedCollection(
  "book_issues",
  {
    bsonType: "object",
    required: ["_id", "studentId", "bookId", "issueDate", "status"],
    additionalProperties: false,
    properties: {
      _id: {
        bsonType: "string",
        description: "Custom issue ID, for example ISS-1001",
      },
      studentId: {
        bsonType: "string",
        description: "Reference to students._id",
      },
      bookId: {
        bsonType: "string",
        description: "Reference to books._id",
      },
      issueDate: {
        bsonType: "date",
      },
      returnDate: {
        anyOf: [
          { bsonType: "date" },
          { bsonType: "null" },
        ],
      },
      status: {
        enum: ["ISSUED", "RETURNED"],
      },
    },
  },
  [
    {
      keys: { studentId: 1 },
      options: { name: "idx_issue_student" },
    },
    {
      keys: { bookId: 1 },
      options: { name: "idx_issue_book" },
    },
    {
      keys: { status: 1 },
      options: { name: "idx_issue_status" },
    },
    {
      keys: { studentId: 1, issueDate: -1 },
      options: { name: "idx_issue_student_date" },
    },
  ]
);

// -------------------------------------------------------------------
// Sample Documents (Compass-compatible)
// -------------------------------------------------------------------
// The documents below are also the seed data inserted by insertMany().
// They can be copied directly into MongoDB Compass if needed.

printSection("2) Preparing sample data");

const departmentDocs = [
  {
    _id: "DEP-1001",
    departmentName: "Computer Science and Engineering",
    hodId: "STF-1001",
  },
  {
    _id: "DEP-1002",
    departmentName: "Information Technology",
    hodId: "STF-1003",
  },
  {
    _id: "DEP-1003",
    departmentName: "Electronics and Communication",
    hodId: "STF-1005",
  },
  {
    _id: "DEP-1004",
    departmentName: "Mathematics",
    hodId: "STF-1007",
  },
  {
    _id: "DEP-1005",
    departmentName: "Business Administration",
    hodId: "STF-1009",
  },
];

const staffDocs = [
  {
    _id: "STF-1001",
    name: "Dr. Ravi Kumar",
    email: "ravi.kumar@college.edu",
    designation: "Professor",
    salary: 95000,
    departmentId: "DEP-1001",
    hodDepartmentId: "DEP-1001",
  },
  {
    _id: "STF-1002",
    name: "Ms. Sneha Iyer",
    email: "sneha.iyer@college.edu",
    designation: "Associate Professor",
    salary: 78000,
    departmentId: "DEP-1001",
  },
  {
    _id: "STF-1003",
    name: "Dr. Arjun Mehta",
    email: "arjun.mehta@college.edu",
    designation: "Professor",
    salary: 93000,
    departmentId: "DEP-1002",
    hodDepartmentId: "DEP-1002",
  },
  {
    _id: "STF-1004",
    name: "Ms. Pooja Sharma",
    email: "pooja.sharma@college.edu",
    designation: "Assistant Professor",
    salary: 65000,
    departmentId: "DEP-1002",
  },
  {
    _id: "STF-1005",
    name: "Dr. Nitin Verma",
    email: "nitin.verma@college.edu",
    designation: "Professor",
    salary: 94000,
    departmentId: "DEP-1003",
    hodDepartmentId: "DEP-1003",
  },
  {
    _id: "STF-1006",
    name: "Mr. Farah Khan",
    email: "farah.khan@college.edu",
    designation: "Lab Instructor",
    salary: 54000,
    departmentId: "DEP-1003",
  },
  {
    _id: "STF-1007",
    name: "Dr. Rahul Singh",
    email: "rahul.singh@college.edu",
    designation: "Professor",
    salary: 92000,
    departmentId: "DEP-1004",
    hodDepartmentId: "DEP-1004",
  },
  {
    _id: "STF-1008",
    name: "Ms. Kavita Das",
    email: "kavita.das@college.edu",
    designation: "Assistant Professor",
    salary: 67000,
    departmentId: "DEP-1004",
  },
  {
    _id: "STF-1009",
    name: "Dr. Manoj Patel",
    email: "manoj.patel@college.edu",
    designation: "Professor",
    salary: 91000,
    departmentId: "DEP-1005",
    hodDepartmentId: "DEP-1005",
  },
  {
    _id: "STF-1010",
    name: "Ms. Ananya Bose",
    email: "ananya.bose@college.edu",
    designation: "Lecturer",
    salary: 58000,
    departmentId: "DEP-1005",
  },
];

const courseDocs = [
  {
    _id: "CRS-1001",
    courseName: "Programming Fundamentals",
    credits: 4,
    departmentId: "DEP-1001",
    studentIds: [],
  },
  {
    _id: "CRS-1002",
    courseName: "Data Structures",
    credits: 4,
    departmentId: "DEP-1001",
    studentIds: [],
  },
  {
    _id: "CRS-1003",
    courseName: "Database Systems",
    credits: 3,
    departmentId: "DEP-1001",
    studentIds: [],
  },
  {
    _id: "CRS-1004",
    courseName: "Operating Systems",
    credits: 4,
    departmentId: "DEP-1002",
    studentIds: [],
  },
  {
    _id: "CRS-1005",
    courseName: "Computer Networks",
    credits: 4,
    departmentId: "DEP-1002",
    studentIds: [],
  },
  {
    _id: "CRS-1006",
    courseName: "Software Engineering",
    credits: 3,
    departmentId: "DEP-1002",
    studentIds: [],
  },
  {
    _id: "CRS-1007",
    courseName: "Digital Electronics",
    credits: 4,
    departmentId: "DEP-1003",
    studentIds: [],
  },
  {
    _id: "CRS-1008",
    courseName: "Microprocessors",
    credits: 4,
    departmentId: "DEP-1003",
    studentIds: [],
  },
  {
    _id: "CRS-1009",
    courseName: "Signal Processing",
    credits: 3,
    departmentId: "DEP-1003",
    studentIds: [],
  },
  {
    _id: "CRS-1010",
    courseName: "Linear Algebra",
    credits: 4,
    departmentId: "DEP-1004",
    studentIds: [],
  },
  {
    _id: "CRS-1011",
    courseName: "Probability and Statistics",
    credits: 4,
    departmentId: "DEP-1004",
    studentIds: [],
  },
  {
    _id: "CRS-1012",
    courseName: "Calculus for Computing",
    credits: 3,
    departmentId: "DEP-1004",
    studentIds: [],
  },
  {
    _id: "CRS-1013",
    courseName: "Principles of Management",
    credits: 4,
    departmentId: "DEP-1005",
    studentIds: [],
  },
  {
    _id: "CRS-1014",
    courseName: "Financial Accounting",
    credits: 3,
    departmentId: "DEP-1005",
    studentIds: [],
  },
  {
    _id: "CRS-1015",
    courseName: "Business Communication",
    credits: 3,
    departmentId: "DEP-1005",
    studentIds: [],
  },
];

const studentNames = [
  "Aman Sharma",
  "Neha Gupta",
  "Rohit Singh",
  "Priya Nair",
  "Karan Malik",
  "Sana Khan",
  "Vivek Joshi",
  "Isha Rao",
  "Aditya Yadav",
  "Meera Nair",
  "Arjun Das",
  "Tanya Kapoor",
  "Kabir Khan",
  "Simran Gill",
  "Nikhil Jain",
  "Riya Verma",
  "Siddharth Bose",
  "Pallavi Menon",
  "Harsh Gupta",
  "Divya Iyer",
];

const cityNames = ["Mumbai", "Pune", "Bengaluru", "Hyderabad", "Chennai"];

const departmentIds = departmentDocs.map(function (department) {
  return department._id;
});

const departmentCourseMap = courseDocs.reduce(function (map, course) {
  if (!map[course.departmentId]) {
    map[course.departmentId] = [];
  }
  map[course.departmentId].push(course._id);
  return map;
}, {});

const studentDocs = studentNames.map(function (name, index) {
  const departmentId = departmentIds[index % departmentIds.length];
  const availableCourses = departmentCourseMap[departmentId];
  const courseIds = [
    availableCourses[index % availableCourses.length],
    availableCourses[(index + 1) % availableCourses.length],
  ];

  if (index % 4 === 0) {
    courseIds.push(availableCourses[(index + 2) % availableCourses.length]);
  }

  return {
    _id: "STU-" + pad(index + 1),
    name: name,
    email: slugify(name) + "@college.edu",
    phone: String(9000000000 + index),
    address: cityNames[index % cityNames.length] + ", India",
    departmentId: departmentId,
    courseIds: Array.from(new Set(courseIds)),
  };
});

courseDocs.forEach(function (course) {
  course.studentIds = studentDocs
    .filter(function (student) {
      return student.courseIds.indexOf(course._id) !== -1;
    })
    .map(function (student) {
      return student._id;
    });
});

const bookTopics = [
  "Clean Code",
  "Refactoring",
  "Design Patterns",
  "Database Systems",
  "Operating Systems Concepts",
  "Computer Networks",
  "Data Structures",
  "Algorithms",
  "Software Engineering",
  "Web Development",
];

const bookAuthors = [
  "Robert C. Martin",
  "Martin Fowler",
  "Erich Gamma",
  "C. J. Date",
  "Abraham Silberschatz",
  "Andrew S. Tanenbaum",
  "Mark Allen Weiss",
  "Thomas H. Cormen",
  "Ian Sommerville",
  "Jon Duckett",
];

const bookDocs = [];
for (let volume = 1; volume <= 3; volume++) {
  bookTopics.forEach(function (topic, index) {
    const serial = (volume - 1) * bookTopics.length + index + 1;
    bookDocs.push({
      _id: "BOK-" + pad(serial),
      title: topic + " - Vol. " + volume,
      author: bookAuthors[index],
      isbn: String(9781000000000 + serial),
      quantity: 3 + ((serial * 7) % 18),
    });
  });
}

const bookIssueDocs = [];
for (let index = 0; index < 20; index++) {
  const issuedOn = new Date(2026, 5, index + 1);
  const isReturned = index % 3 !== 0;
  bookIssueDocs.push({
    _id: "ISS-" + pad(index + 1),
    studentId: studentDocs[index % studentDocs.length]._id,
    bookId: bookDocs[index % bookDocs.length]._id,
    issueDate: issuedOn,
    returnDate: isReturned ? new Date(2026, 5, index + 6) : null,
    status: isReturned ? "RETURNED" : "ISSUED",
  });
}

printSection("3) Inserting sample data");

appDb.departments.insertMany(departmentDocs);
appDb.staff.insertMany(staffDocs);
appDb.courses.insertMany(courseDocs);
appDb.students.insertMany(studentDocs);
appDb.books.insertMany(bookDocs);
appDb.book_issues.insertMany(bookIssueDocs);

print("Seed data inserted successfully.");
printjson({
  departments: appDb.departments.countDocuments(),
  staff: appDb.staff.countDocuments(),
  courses: appDb.courses.countDocuments(),
  students: appDb.students.countDocuments(),
  books: appDb.books.countDocuments(),
  book_issues: appDb.book_issues.countDocuments(),
});

// -------------------------------------------------------------------
// MongoDB Compass-compatible sample documents
// -------------------------------------------------------------------
/*
Departments sample document:
{
  "_id": "DEP-1001",
  "departmentName": "Computer Science and Engineering",
  "hodId": "STF-1001"
}

Staff sample document:
{
  "_id": "STF-1001",
  "name": "Dr. Ravi Kumar",
  "email": "ravi.kumar@college.edu",
  "designation": "Professor",
  "salary": 95000,
  "departmentId": "DEP-1001",
  "hodDepartmentId": "DEP-1001"
}

Student sample document:
{
  "_id": "STU-0001",
  "name": "Aman Sharma",
  "email": "aman.sharma@college.edu",
  "phone": "9000000000",
  "address": "Mumbai, India",
  "departmentId": "DEP-1001",
  "courseIds": ["CRS-1001", "CRS-1002", "CRS-1003"]
}

Course sample document:
{
  "_id": "CRS-1001",
  "courseName": "Programming Fundamentals",
  "credits": 4,
  "departmentId": "DEP-1001",
  "studentIds": ["STU-0001", "STU-0002"]
}

Book sample document:
{
  "_id": "BOK-0001",
  "title": "Clean Code - Vol. 1",
  "author": "Robert C. Martin",
  "isbn": "9781000000001",
  "quantity": 10
}

Book issue sample document:
{
  "_id": "ISS-0001",
  "studentId": "STU-0001",
  "bookId": "BOK-0001",
  "issueDate": new Date("2026-06-01T00:00:00Z"),
  "returnDate": null,
  "status": "ISSUED"
}
*/

// -------------------------------------------------------------------
// CRUD examples for each module
// -------------------------------------------------------------------
// These are examples only. Uncomment the lines you want to run.

/*
----------------------
Student Management CRUD
----------------------
appDb.students.insertOne({
  _id: "STU-9999",
  name: "Test Student",
  email: "test.student@college.edu",
  phone: "9123456789",
  address: "Test City, India",
  departmentId: "DEP-1001",
  courseIds: ["CRS-1001", "CRS-1002"],
});
appDb.students.findOne({ _id: "STU-0001" });
appDb.students.find({ departmentId: "DEP-1001" }).pretty();
appDb.students.updateOne(
  { _id: "STU-0001" },
  { $set: { phone: "9998887776", address: "Updated City, India" } }
);
appDb.students.updateMany(
  { departmentId: "DEP-1001" },
  { $addToSet: { courseIds: "CRS-1003" } }
);
appDb.students.deleteOne({ _id: "STU-9999" });
appDb.students.deleteMany({ departmentId: "DEP-1005" });

----------------------
Staff Management CRUD
----------------------
appDb.staff.insertOne({
  _id: "STF-9999",
  name: "Test Staff",
  email: "test.staff@college.edu",
  designation: "Lecturer",
  salary: 50000,
  departmentId: "DEP-1002",
});
appDb.staff.findOne({ _id: "STF-1001" });
appDb.staff.find({ departmentId: "DEP-1001" }).pretty();
appDb.staff.updateOne(
  { _id: "STF-1002" },
  { $set: { designation: "Senior Associate Professor" } }
);
appDb.staff.updateMany(
  { departmentId: "DEP-1001" },
  { $inc: { salary: 2500 } }
);
appDb.staff.deleteOne({ _id: "STF-9999" });
appDb.staff.deleteMany({ departmentId: "DEP-1005" });

----------------------
Department Management CRUD
----------------------
appDb.departments.insertOne({
  _id: "DEP-9999",
  departmentName: "Test Department",
  hodId: "STF-1001",
});
appDb.departments.findOne({ _id: "DEP-1001" });
appDb.departments.find({}).pretty();
appDb.departments.updateOne(
  { _id: "DEP-1001" },
  { $set: { departmentName: "Computer Science" } }
);
appDb.departments.updateMany(
  {},
  { $set: { note: "Example field for testing" } }
);
appDb.departments.deleteOne({ _id: "DEP-9999" });
appDb.departments.deleteMany({ departmentName: /Test/ });

----------------------
Course Management CRUD
----------------------
appDb.courses.insertOne({
  _id: "CRS-9999",
  courseName: "Test Course",
  credits: 2,
  departmentId: "DEP-1001",
  studentIds: [],
});
appDb.courses.findOne({ _id: "CRS-1001" });
appDb.courses.find({ departmentId: "DEP-1001" }).pretty();
appDb.courses.updateOne(
  { _id: "CRS-1001" },
  { $set: { credits: 5 } }
);
appDb.courses.updateMany(
  { departmentId: "DEP-1001" },
  { $addToSet: { studentIds: "STU-0001" } }
);
appDb.courses.deleteOne({ _id: "CRS-9999" });
appDb.courses.deleteMany({ credits: { $lt: 3 } });

----------------------
Book Management CRUD
----------------------
appDb.books.insertOne({
  _id: "BOK-9999",
  title: "Test Book",
  author: "Test Author",
  isbn: "9781999999999",
  quantity: 1,
});
appDb.books.findOne({ _id: "BOK-0001" });
appDb.books.find({ quantity: { $lt: 5 } }).pretty();
appDb.books.updateOne(
  { _id: "BOK-0001" },
  { $inc: { quantity: 2 } }
);
appDb.books.updateMany(
  { quantity: { $lt: 5 } },
  { $set: { quantity: 5 } }
);
appDb.books.deleteOne({ _id: "BOK-9999" });
appDb.books.deleteMany({ quantity: 0 });

----------------------
Book Issue Management CRUD
----------------------
appDb.book_issues.insertOne({
  _id: "ISS-9999",
  studentId: "STU-0001",
  bookId: "BOK-0001",
  issueDate: new Date(),
  returnDate: null,
  status: "ISSUED",
});
appDb.book_issues.findOne({ _id: "ISS-0001" });
appDb.book_issues.find({ studentId: "STU-0001" }).pretty();
appDb.book_issues.updateOne(
  { _id: "ISS-0001" },
  {
    $set: {
      returnDate: new Date(),
      status: "RETURNED",
    },
  }
);
appDb.book_issues.updateMany(
  { status: "ISSUED" },
  { $set: { status: "RETURNED" } }
);
appDb.book_issues.deleteOne({ _id: "ISS-9999" });
appDb.book_issues.deleteMany({ status: "RETURNED" });
*/

// -------------------------------------------------------------------
// Sample aggregation queries
// -------------------------------------------------------------------
/*
1) Number of students in each department
appDb.students.aggregate([
  {
    $lookup: {
      from: "departments",
      localField: "departmentId",
      foreignField: "_id",
      as: "department",
    },
  },
  { $unwind: "$department" },
  {
    $group: {
      _id: "$department.departmentName",
      studentCount: { $sum: 1 },
    },
  },
  { $sort: { studentCount: -1 } },
]).pretty();

2) Average salary by department
appDb.staff.aggregate([
  {
    $group: {
      _id: "$departmentId",
      averageSalary: { $avg: "$salary" },
      staffCount: { $sum: 1 },
    },
  },
  {
    $lookup: {
      from: "departments",
      localField: "_id",
      foreignField: "_id",
      as: "department",
    },
  },
  { $unwind: "$department" },
  {
    $project: {
      _id: 0,
      departmentName: "$department.departmentName",
      averageSalary: 1,
      staffCount: 1,
    },
  },
]).pretty();

3) Books currently issued and not yet returned
appDb.book_issues.aggregate([
  { $match: { status: "ISSUED" } },
  {
    $lookup: {
      from: "students",
      localField: "studentId",
      foreignField: "_id",
      as: "student",
    },
  },
  {
    $lookup: {
      from: "books",
      localField: "bookId",
      foreignField: "_id",
      as: "book",
    },
  },
  { $unwind: "$student" },
  { $unwind: "$book" },
  {
    $project: {
      _id: 1,
      studentName: "$student.name",
      bookTitle: "$book.title",
      issueDate: 1,
      status: 1,
    },
  },
]).pretty();

4) Course enrollment count
appDb.courses.aggregate([
  {
    $project: {
      courseName: 1,
      enrollmentCount: { $size: "$studentIds" },
    },
  },
  { $sort: { enrollmentCount: -1 } },
]).pretty();
*/

// -------------------------------------------------------------------
// Sample lookup queries to join collections
// -------------------------------------------------------------------
/*
1) Students with department and enrolled courses
appDb.students.aggregate([
  {
    $lookup: {
      from: "departments",
      localField: "departmentId",
      foreignField: "_id",
      as: "department",
    },
  },
  {
    $lookup: {
      from: "courses",
      localField: "courseIds",
      foreignField: "_id",
      as: "courses",
    },
  },
  {
    $project: {
      _id: 1,
      name: 1,
      email: 1,
      department: { $arrayElemAt: ["$department.departmentName", 0] },
      courses: "$courses.courseName",
    },
  },
]).pretty();

2) Staff with department details
appDb.staff.aggregate([
  {
    $lookup: {
      from: "departments",
      localField: "departmentId",
      foreignField: "_id",
      as: "department",
    },
  },
  { $unwind: "$department" },
  {
    $project: {
      _id: 1,
      name: 1,
      designation: 1,
      salary: 1,
      departmentName: "$department.departmentName",
    },
  },
]).pretty();

3) Department with HOD staff information
appDb.departments.aggregate([
  {
    $lookup: {
      from: "staff",
      localField: "hodId",
      foreignField: "_id",
      as: "hod",
    },
  },
  { $unwind: "$hod" },
  {
    $project: {
      _id: 1,
      departmentName: 1,
      hodName: "$hod.name",
      hodDesignation: "$hod.designation",
      hodEmail: "$hod.email",
    },
  },
]).pretty();

4) Book issue history with student and book data
appDb.book_issues.aggregate([
  {
    $lookup: {
      from: "students",
      localField: "studentId",
      foreignField: "_id",
      as: "student",
    },
  },
  {
    $lookup: {
      from: "books",
      localField: "bookId",
      foreignField: "_id",
      as: "book",
    },
  },
  { $unwind: "$student" },
  { $unwind: "$book" },
  {
    $project: {
      _id: 1,
      studentName: "$student.name",
      studentDepartmentId: "$student.departmentId",
      bookTitle: "$book.title",
      status: 1,
      issueDate: 1,
      returnDate: 1,
    },
  },
  { $sort: { issueDate: -1 } },
]).pretty();
*/

printSection("4) Setup complete");
print("College Management System MongoDB database is ready.");
print("Database name: college_management_db");
