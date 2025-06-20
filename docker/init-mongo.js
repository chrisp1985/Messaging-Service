db = db.getSiblingDB('testdb');

db.createUser({
  user: "myuser",
  pwd: "mypassword",
  roles: [
    {
      role: "readWrite",
      db: "testdb"
    }
  ]
});