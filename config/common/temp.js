//use dokimion
db = db.getSiblingDB('dokimion')

db.createUser(
{
   user: "dokimion",
   pwd: "$2b$12$XouFVDvizUZOgqkKQlwG2.4.Cmdc1025smEd5rOGoUWDC3IzZiB1q",
   roles: [
      { role: "userAdmin", db: "dokimion" },
      { role: "readWrite", db: "dokimion" }
   ]
})