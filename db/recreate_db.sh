#! /bin/bash
rm -f database.db
cat schema.sql | sqlite3 database.db
